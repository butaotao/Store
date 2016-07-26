package com.dachen.medicine.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.dachen.medicine.R;
import com.dachen.medicine.common.utils.MyLog;




/**
 * @author znouy 图案锁
 */
public class LockPatternView extends View {

	private static final int POINT_SIZE = 4;// 选中点的最小数量

	private Matrix matrix = new Matrix();

	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔

	public Point[][] points = new Point[3][3];// 存储九个点的集合
	private boolean isInit /** 初始化完成 */
	, isSelect/** 是否选中了 */
	, isFinish/** 是否绘制结束了 */
	, movingNoPoint/** 移动但不是九宫格的点 */
	;

	private OnPatterChangeLisrener onPatterChangeLisrener;

	private float wight, height, offsetsX, offsetsY/** x和y方向的偏移量 */
	, movingX, movingY;
	private Bitmap pointNormal, pointPressed, pointError, lineError,
			linePressed;

	/** 图片资源半径 */
	private int bitmapR;

	/**按下点的集合*/
	private List<Point> pointList = new ArrayList<Point>();
	/**显示提示点的集合*/
	private List<GuideCell> IndicatePointList = new ArrayList<GuideCell>();

	/**
	 * Timer可以执行的任务，实现了Runable接口
	 */
	/**
	 * 可在后台线程中执行的任务，可以看成一个定时器，可以调度TimerTask
	 */
	private TimerTask task;
	/**
	 * 清除痕迹的时间
	 */
	private Timer timer = new Timer();
	private long CLEAR_TIME = 1000;

	public LockPatternView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (!isInit) {
			initPoints();
		}
		// 画点
		points2Canvas(canvas);

		// 画线
		if (pointList.size() > 0) {
			Point a = pointList.get(0);
			for (int i = 0; i < pointList.size(); i++) {
				Point b = pointList.get(i);
				line2Canvas(canvas, a, b);
				a = b;
			}
			// 不是九宫格的店也要绘制，即绘制鼠标坐标
			if (movingNoPoint) {
				line2Canvas(canvas, a, new Point(movingX, movingY));
			}
		}

	}

	/**
	 * 绘制点到画布上
	 * 
	 * @param canvas
	 *            画布
	 */
	private void points2Canvas(Canvas canvas) {
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[i].length; j++) {
				Point point = points[i][j];
				if (point.state == Point.STATE_NORMAL) {
					canvas.drawBitmap(pointNormal, point.x - bitmapR, point.y
							- bitmapR, paint);
				} else if (point.state == Point.STATE_PRESSED) {
					canvas.drawBitmap(pointPressed, point.x -bitmapR, point.y
							- bitmapR, paint);
				} else {
					canvas.drawBitmap(pointError, point.x - bitmapR, point.y
							- bitmapR, paint);
				}
			}
		}
	}

	/**
	 * 划线
	 * 
	 * @param canvas
	 *            画布
	 * @param a
	 *            第一个点
	 * @param b
	 *            第二个点
	 */
	private void line2Canvas(Canvas canvas, Point a, Point b) {

		double lineLenght = Point.distance(a.x, a.y, b.x, b.y);// 线的长度
		float degrees = Point.getDegrees(a, b);
		canvas.rotate(degrees, a.x, a.y);

		if (a.state == Point.STATE_PRESSED) {
			matrix.setScale((float) lineLenght / linePressed.getWidth(), 1);// 缩放
			matrix.postTranslate(a.x - linePressed.getWidth() / 2, a.y
					- linePressed.getHeight() / 2);// 平移
			canvas.drawBitmap(linePressed, matrix, paint);
		} else {
			matrix.setScale((float) lineLenght / lineError.getWidth(), 1);
			matrix.postTranslate(a.x - lineError.getWidth() / 2, a.y
					- lineError.getHeight() / 2);
			canvas.drawBitmap(lineError, matrix, paint);
		}
		canvas.rotate(-degrees, a.x, a.y);
	}

	/**
	 * 1. 初始化点
	 */
	private void initPoints() {
		// 1.获取布局
		wight = getWidth();
		height = getHeight();
		movingNoPoint = true;

		// 2.偏移量
		if (wight > height) {
			// 横屏
			offsetsX = (wight - height) / 2;
			wight = height;
		} else {
			// 竖屏
			offsetsY = (height - wight) / 2;
			height = wight;
		}

		// 3.图片资源
		pointPressed = BitmapFactory.decodeResource(getResources(),
				R.drawable.locus_round_click);
		pointNormal = BitmapFactory.decodeResource(getResources(),
				R.drawable.locus_round_original);
		pointError = BitmapFactory.decodeResource(getResources(),
				R.drawable.locus_round_click);

		linePressed = BitmapFactory.decodeResource(getResources(),
				R.drawable.locus_line);
		lineError = BitmapFactory.decodeResource(getResources(),
				R.drawable.locus_line);
		

		// 4.点的坐标
		points[0][0] = new Point(offsetsX + wight / 4, offsetsY + wight / 4);
		points[0][1] = new Point(offsetsX + wight / 2, offsetsY + wight / 4);
		points[0][2] = new Point(offsetsX + wight - wight / 4, offsetsY + wight
				/ 4);

		points[1][0] = new Point(offsetsX + wight / 4, offsetsY + wight / 2);
		points[1][1] = new Point(offsetsX + wight / 2, offsetsY + wight / 2);
		points[1][2] = new Point(offsetsX + wight - wight / 4, offsetsY + wight
				/ 2);

		points[2][0] = new Point(offsetsX + wight / 4, offsetsY + wight - wight
				/ 4);
		points[2][1] = new Point(offsetsX + wight / 2, offsetsY + wight - wight
				/ 4);
		points[2][2] = new Point(offsetsX + wight - wight / 4, offsetsY + wight
				- wight / 4);

		// 5.图片资源半径
		bitmapR = pointNormal.getWidth() / 2;

		// 6.设置密码
		int index = 0;
		for (Point[] points : this.points) {
			for (Point point : points) {
				point.index = index;
				index++;
			}
		}

		isInit = true;// 初始化完成
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	
		movingNoPoint = false;
		isFinish = false;

		movingX = event.getX();
		movingY = event.getY();

		Point point = null;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			resetPoint();
			point = checkSelectedPoint();// 检查按下的点是否是九宫格里的点

			if (point != null) {
				isSelect = true;
			}
			break;
		case MotionEvent.ACTION_UP:
			isFinish = true;// 只有抬起鼠标，绘制才结束
			isSelect = false;

			// 抬起鼠标，清楚密码轨迹 ，也有可能是重置点的状态 TODO
			// clearPassword();
			// resetPoint();

			break;
		case MotionEvent.ACTION_MOVE:

			if (isSelect) {
				point = checkSelectedPoint();
			}

			if (point == null) {
				movingNoPoint = true;
			}
			break;
		}

		// 选中重复检查
		if (!isFinish && isSelect && point != null) {
			// 重复即交叉点
			if (pointList.contains(point)) {
				movingNoPoint = true;

			} else {
				// 新点
				point.state = Point.STATE_PRESSED;
				pointList.add(point);// 将选中的点加入选中点的集合
			}
		}
		// 绘制结束
		if (isFinish) {
			MyLog.log("isFinish=" + isFinish);
			// 绘制不成立
			if (pointList.size() < 2) {
				MyLog.log("ontouch isFinish 绘制不成立" + pointList.size());
				resetPoint();
				// 绘制错误
			} else if (pointList.size() < POINT_SIZE && pointList.size() > 1) {
				MyLog.log("ontouch isFinish 长度不够" + pointList.size());
				Toast.makeText(getContext(), "长度不够", 1).show();
				errorPoint();
				clearPassword();// 清楚密码

			} else {
				MyLog.log("ontouch isFinish 绘制成功");
				// 绘制成功
				if (onPatterChangeLisrener != null) {
					StringBuffer passWord = new StringBuffer();// 存储密码的集合
					for (Point p : pointList) {
						passWord.append(p.index);
					}
					String psd = passWord.toString();
					//将密码传递给activity
					onPatterChangeLisrener.getStringPassword(psd);
					
					if (onPatterChangeLisrener.isPassword()) {
						for (int i = 0; i < pointList.size(); i++) {
							pointList.get(i).state = Point.STATE_PRESSED;
							// 清楚密码轨迹
							clearPassword(0);
						}
					} else {
						for (int i = 0; i < pointList.size(); i++) {
							pointList.get(i).state = Point.STATE_ERROR;
							// 清楚密码轨迹
							clearPassword();
						}
					}
				}
			}
		}
		// 刷新view
		postInvalidate();
		return true;
	}

	/** 重置点的状态 */
	public void resetPoint() {
		for (Point point : pointList) {
			point.state = Point.STATE_NORMAL;
		}
		pointList.clear();
	}

	/** 设置绘制错误 */
	public void errorPoint() {
		for (Point point : pointList) {
			point.state = Point.STATE_ERROR;
		}
	}

	/** 检查是否选中 */
	private Point checkSelectedPoint() {

		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[i].length; j++) {
				Point point = points[i][j];

				if (Point.with(point.x, point.y, bitmapR, movingX, movingY)) {
					return point;
				}
			}
		}

		return null;
	}

	/**
	 * 清除密码
	 */
	public void clearPassword() {

		clearPassword(CLEAR_TIME);
	}

	private void clearPassword(final long time) {

		if (time > 1) {
			if (task != null) {
				task.cancel();

			}

			postInvalidate();
			task = new TimerTask() {
				public void run() {
					resetPoint();
					postInvalidate();
				}
			};

			timer.schedule(task, time);
		} else {
			resetPoint();
			postInvalidate();
		}

	}

	/**
	 * @author znouy
	 * @des 自定义提示图案
	 */
	public static class GuideCell {
		int row;// 行
		int column;// 列

		static GuideCell[][] sCells = new GuideCell[3][3];
		static {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					sCells[i][j] = new GuideCell(i, j);
				}
			}
		}

		private GuideCell(int row, int column) {

			this.row = row;
			this.column = column;
		}

		/** 获取行 */
		public int getRow() {
			return row;
		}

		/** 获取列 */
		public int getColumn() {
			return column;
		}

		@Override
		public String toString() {
			return "GuideCell [row=" + row + ", column=" + column + "]";
		}

		/**
		 * @param i 行
		 * @param j 列
		 * @return 一个提示点
		 */
		public static GuideCell of(int i, int j) {
			return sCells[i][j];
		}
	}

	/**
	 * 自定义点对象
	 */
	public static class Point {
		public static int STATE_NORMAL = 0;// 正常
		public static int STATE_PRESSED = 1;// 选中
		public static int STATE_ERROR = 2;// 错误
		public float x, y;
		public int state = 0, index = 0;

		public Point(float x, float y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * 鼠标当前是否包含在一个点中
		 * 
		 * @param pointX
		 *            点的X
		 * @param pointY
		 *            点的Y
		 * @param r
		 *            点的半径
		 * @param movingX
		 *            鼠标的X
		 * @param movingY
		 *            鼠标Y
		 * @return 是否包含
		 */
		public static boolean with(float pointX, float pointY, float r,
				float movingX, float movingY) {
			// 开方
			return Math.sqrt((movingX - pointX) * (movingX - pointX)
					+ (movingY - pointY) * (movingY - pointY)) < r;
		}

		/**
		 * 获取两点间距离
		 * 
		 * @param x1
		 * @param y1
		 * @return
		 */
		public static double distance(double x1, double y1, double x2, double y2) {
			return Math.sqrt(Math.abs(x1 - x2) * Math.abs(x1 - x2)
					+ Math.abs(y1 - y2) * Math.abs(y1 - y2));
		}

		public static float getDegrees(Point a, Point b) {

			float ax = a.x;// a.index % 3;
			float ay = a.y;// a.index / 3;
			float bx = b.x;// b.index % 3;
			float by = b.y;// b.index / 3;
			float degrees = 0;
			if (bx == ax) //
			{
				if (by > ay) //
				{
					degrees = 90;
				} else if (by < ay) //
				{
					degrees = 270;
				}
			} else if (by == ay) //
			{
				if (bx > ax) //
				{
					degrees = 0;
				} else if (bx < ax) //
				{
					degrees = 180;
				}
			} else {
				if (bx > ax) //
				{
					if (by > ay) //
					{
						degrees = 0;
						degrees = degrees
								+ switchDegrees(Math.abs(by - ay),
										Math.abs(bx - ax));
					} else if (by < ay) //
					{
						degrees = 360;
						degrees = degrees
								- switchDegrees(Math.abs(by - ay),
										Math.abs(bx - ax));
					}

				} else if (bx < ax) //
				{
					if (by > ay) //
					{
						degrees = 90;
						degrees = degrees
								+ switchDegrees(Math.abs(bx - ax),
										Math.abs(by - ay));
					} else if (by < ay) //
					{
						degrees = 270;
						degrees = degrees
								- switchDegrees(Math.abs(bx - ax),
										Math.abs(by - ay));
					}

				}

			}
			return degrees;
		}

		private static float switchDegrees(float x, float y) {
			return (float) Math.toDegrees(Math.atan2(x, y));
		}

	}

	/** 图案改变监听器接口 */
	public interface OnPatterChangeLisrener {

		public void getStringPassword(String password);

		public boolean isPassword();
		
		

	}

	public void setOnPatterChangeLisrener(OnPatterChangeLisrener listener) {
		this.onPatterChangeLisrener = listener;

	}

	/**
	 * @param password
	 * @return 将数字密码转成提示点的集合
	 */
	public static List<LockPatternView.GuideCell> stringToPattern(String password) {
		List<LockPatternView.GuideCell> result = new ArrayList<LockPatternView.GuideCell>();

		 char[] ch= password.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			int b = ch[i]-48;
			System.out.println(b);
			result.add(LockPatternView.GuideCell.of(b / 3, b % 3));
		}
		return result;
	}
}
