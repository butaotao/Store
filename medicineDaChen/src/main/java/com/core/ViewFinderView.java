package com.core;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.dachen.medicine.app.MedicineApplication;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.google.zxing.ResultPoint;
import com.mining.app.zxing.camera.CameraManager;

import java.util.Collection;
import java.util.HashSet;

import me.dm7.barcodescanner.core.R;

public class ViewFinderView extends View implements IViewFinder {
    private static final String TAG = "ViewFinderView";
    private static final int CORNER_WIDTH = 60;
    private Rect mFramingRect;
    private static final int OPAQUE = 0xFF;
    private Paint paint;
    private static final int MIN_FRAME_WIDTH = 240;
    private static final int MIN_FRAME_HEIGHT = 240;
    private static final int TEXT_SIZE = 16;
    private  int resultPointColor;
    boolean isFirst;
    private Bitmap resultBitmap;
    /**
     * �������ɨ�������ľ���
     */
    private static final int TEXT_PADDING_TOP = 5;
    int color = getResources().getColor(com.dachen.medicine.R.color.color_5bc7b9);
    private static final float LANDSCAPE_WIDTH_RATIO = 5f/8;
    private static final float LANDSCAPE_HEIGHT_RATIO = 5f/8;
    private static final int LANDSCAPE_MAX_FRAME_WIDTH = (int) (1920 * LANDSCAPE_WIDTH_RATIO); // = 5/8 * 1920
    private static final int LANDSCAPE_MAX_FRAME_HEIGHT = (int) (1080 * LANDSCAPE_HEIGHT_RATIO); // = 5/8 * 1080
    private static final int MIDDLE_LINE_PADDING = 3;
    private static final float PORTRAIT_WIDTH_RATIO = 8f/8;
    private static final float PORTRAIT_HEIGHT_RATIO = 3f/8;
    private static final int PORTRAIT_MAX_FRAME_WIDTH = (int) (1080 * PORTRAIT_WIDTH_RATIO); // = 7/8 * 1080
    private static final int PORTRAIT_MAX_FRAME_HEIGHT = (int) (1920 * PORTRAIT_HEIGHT_RATIO); // = 3/8 * 1920
    private static final int SPEEN_DISTANCE = 5;
    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    private int scannerAlpha;
    private static final int POINT_SIZE = 10;
    private static final long ANIMATION_DELAY = 80l;
    private static final int MIDDLE_LINE_WIDTH = 3;
     int slideTop;
      int maskColor;
      int resultColor;
     int slideBottom;
    private static float density;
    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;
    private final int mDefaultLaserColor = getResources().getColor(R.color.viewfinder_laser);
    private final int mDefaultMaskColor = getResources().getColor(R.color.viewfinder_mask);
    private final int mDefaultBorderColor = getResources().getColor(R.color.viewfinder_border);
    private final int mDefaultBorderStrokeWidth = getResources().getInteger(R.integer.viewfinder_border_width);
    private final int mDefaultBorderLineLength = getResources().getInteger(R.integer.viewfinder_border_length);
    String text1 = getResources().getString(com.dachen.medicine.R.string.show_scan_line1);
    String text2 = getResources().getString(com.dachen.medicine.R.string.show_scan_line2);
    String text3 = getResources().getString(com.dachen.medicine.R.string.show_scan_line3);
    String text4 = getResources().getString(com.dachen.medicine.R.string.show_scan_line4);
    protected Paint mLaserPaint;
    protected Paint mFinderMaskPaint;
    protected Paint mBorderPaint;
    protected int mBorderLineLength;
    private int ScreenRate;
    public int linewidth = 3;
    Rect framingRect;
    Context context;
    public ViewFinderView(Context context) {
        super(context);
        init(context);
    }

    public ViewFinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        density = context.getResources().getDisplayMetrics().density;
        init(context);
    }

    private void init(Context context) {
        //set up laser paint
        mLaserPaint = new Paint();
        possibleResultPoints = new HashSet<ResultPoint>(5);
        ScreenRate = (int)(20 * density);
        mLaserPaint.setColor(mDefaultLaserColor);
        mLaserPaint.setStyle(Paint.Style.FILL);
        paint = new Paint();
        //finder mask paint
        mFinderMaskPaint = new Paint();
        mFinderMaskPaint.setColor(mDefaultMaskColor);

        //border paint
        mBorderPaint = new Paint();
        mBorderPaint.setColor(mDefaultBorderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mDefaultBorderStrokeWidth);

        mBorderLineLength = mDefaultBorderLineLength;
    }

    public void setLaserColor(int laserColor) {
        mLaserPaint.setColor(laserColor);
    }
    public void setMaskColor(int maskColor) {
        mFinderMaskPaint.setColor(maskColor);
    }
    public void setBorderColor(int borderColor) {
        mBorderPaint.setColor(borderColor);
    }
    public void setBorderStrokeWidth(int borderStrokeWidth) {
        mBorderPaint.setStrokeWidth(borderStrokeWidth);
    }
    public void setBorderLineLength(int borderLineLength) {
        mBorderLineLength = borderLineLength;
    }

    public void setupViewFinder() {
        updateFramingRect();
        invalidate();
    }

    public Rect getFramingRect() {
        return mFramingRect;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(mFramingRect == null) {
            return;
        }
       drawCode(canvas);
     /*  drawViewFinderMask(canvas);
        drawViewFinderBorder(canvas);
        drawLaser(canvas);*/
    }

    public void drawViewFinderMask(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        canvas.drawRect(0, 0, width, mFramingRect.top, mFinderMaskPaint);
        canvas.drawRect(0, mFramingRect.top, mFramingRect.left, mFramingRect.bottom + 1, mFinderMaskPaint);
        canvas.drawRect(mFramingRect.right + 1, mFramingRect.top, width, mFramingRect.bottom + 1, mFinderMaskPaint);
        canvas.drawRect(0, mFramingRect.bottom + 1, width, height, mFinderMaskPaint);
    }

    public void drawViewFinderBorder(Canvas canvas) {
        canvas.drawLine(mFramingRect.left - 1, mFramingRect.top - 1,
                mFramingRect.left - 1, mFramingRect.top - 1 + mBorderLineLength, mBorderPaint);
        canvas.drawLine(mFramingRect.left - 1, mFramingRect.top - 1,
                mFramingRect.left - 1 + mBorderLineLength, mFramingRect.top - 1, mBorderPaint);

        canvas.drawLine(mFramingRect.left - 1, mFramingRect.bottom + 1,
                mFramingRect.left - 1, mFramingRect.bottom + 1 - mBorderLineLength, mBorderPaint);
        canvas.drawLine(mFramingRect.left - 1, mFramingRect.bottom + 1,
                mFramingRect.left - 1 + mBorderLineLength, mFramingRect.bottom + 1, mBorderPaint);

        canvas.drawLine(mFramingRect.right + 1, mFramingRect.top - 1,
                mFramingRect.right + 1, mFramingRect.top - 1 + mBorderLineLength, mBorderPaint);
        canvas.drawLine(mFramingRect.right + 1, mFramingRect.top - 1,
                mFramingRect.right + 1 - mBorderLineLength, mFramingRect.top - 1, mBorderPaint);

        canvas.drawLine(mFramingRect.right + 1, mFramingRect.bottom + 1,
                mFramingRect.right + 1, mFramingRect.bottom + 1 - mBorderLineLength, mBorderPaint);
        canvas.drawLine(mFramingRect.right + 1, mFramingRect.bottom + 1, mFramingRect.right + 1 - mBorderLineLength, mFramingRect.bottom + 1, mBorderPaint);
    }

    public Rect getFramingRectConst(Canvas canvas) {

      //  Point screenResolution = new Point(widths,heights);


        int x = MedicineApplication.widths;
        int y = MedicineApplication.heights;
        if (framingRect == null) {

            int width = y* 13 / 36;

            int height = y * 13 / 36;

            int leftOffset = (x - width) / 2;
            int topOffset = (y - height) / 2;
            int leftPad = (topOffset- height/2);
            int rightPad = leftOffset + width;
            int botomPad = topOffset + height/2;
            framingRect = new Rect(leftOffset, leftPad, rightPad,
                    botomPad);


        }
        return framingRect;
    }
    public void drawCode(Canvas canvas){

        Rect frame = getFramingRectConst(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        if (frame == null) {
            return;
        }

        //��ʼ���м��߻��������ϱߺ����±�
        if(!isFirst){
            isFirst = true;
            slideTop = frame.top;
            slideBottom = frame.bottom;
        }

        //��ȡ��Ļ�Ŀ�͸�

        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
                paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);



        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(OPAQUE);
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        } else {

            //��ɨ�����ϵĽǣ��ܹ�8������
            paint.setColor(color);
            canvas.drawRect(frame.left, frame.top, frame.left + linewidth,
                    frame.top + CORNER_WIDTH, paint);
            canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top
                    + linewidth, paint);
            canvas.drawRect(frame.right - linewidth, frame.top, frame.right,
                    frame.top + CORNER_WIDTH, paint);
            canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top
                    + linewidth, paint);
            canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
                    + linewidth, frame.bottom, paint);
            canvas.drawRect(frame.left, frame.bottom - linewidth,
                    frame.left + CORNER_WIDTH, frame.bottom, paint);
            canvas.drawRect(frame.right - linewidth, frame.bottom - CORNER_WIDTH,
                    frame.right, frame.bottom, paint);
            canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - linewidth,
                    frame.right, frame.bottom, paint);

            slideTop += SPEEN_DISTANCE;
            if(slideTop >= frame.bottom){
                slideTop = frame.top;
            }
            canvas.drawRect(frame.left + MIDDLE_LINE_PADDING,
                    slideTop - MIDDLE_LINE_WIDTH/2, frame.right - MIDDLE_LINE_PADDING,
                    slideTop + MIDDLE_LINE_WIDTH/2, paint);

            paint.setColor(Color.WHITE);
            paint.setTextSize(TEXT_SIZE * 3);
            paint.setAlpha(0x40);
            paint.setTypeface(Typeface.create("System", Typeface.BOLD));
            float strwidth1 = paint.measureText(text1);
            float strwidth2 = paint.measureText(text2);
            float strwidth3 = paint.measureText(text3);
            float strwidth4 = paint.measureText(text4);
            int alert = SharedPreferenceUtil.getInt("show_alert", 0);
            if (1==alert) {
                canvas.drawText(text1,
                        (frame.right-frame.left)/2+frame.left-strwidth1/2,
                        (float) (frame.bottom + 50), paint);
                canvas.drawText(text2,
                        (frame.right-frame.left)/2+
                                frame.left-strwidth2/2, (float) (frame.bottom + 100), paint);

            }else {
                canvas.drawText(text3,
                        (frame.right-frame.left)/2+frame.left-strwidth3/2,
                        (float) (frame.bottom + 50), paint);
                canvas.drawText(text4,
                        (frame.right-frame.left)/2+frame.left-strwidth4/2, (
                                float) (frame.bottom + 100), paint);
            }
            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<ResultPoint>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(OPAQUE);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 6.0f, paint);
                }
            }
            if (currentLast != null) {
                paint.setAlpha(OPAQUE / 2);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 3.0f, paint);
                }
            }


            //ֻˢ��ɨ�������ݣ�����ط���ˢ��
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
                    frame.right, frame.bottom);

        }
    }
    public void drawLaser(Canvas canvas) {
        // Draw a red "laser scanner" line through the middle to show decoding is active
        mLaserPaint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
        scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
        int middle = mFramingRect.height() / 2 + mFramingRect.top;
        canvas.drawRect(mFramingRect.left + 2, middle - 1, mFramingRect.right - 1, middle + 2, mLaserPaint);

        postInvalidateDelayed(ANIMATION_DELAY,
                mFramingRect.left - POINT_SIZE,
                mFramingRect.top - POINT_SIZE,
                mFramingRect.right + POINT_SIZE,
                mFramingRect.bottom + POINT_SIZE);
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        updateFramingRect();
    }

    public synchronized void updateFramingRect() {
        Point viewResolution = new Point(getWidth(), getHeight());
        int width;
        int height;
        int orientation = DisplayUtils.getScreenOrientation(getContext());

        if(orientation != Configuration.ORIENTATION_PORTRAIT) {
            width = findDesiredDimensionInRange(LANDSCAPE_WIDTH_RATIO, viewResolution.x, MIN_FRAME_WIDTH, LANDSCAPE_MAX_FRAME_WIDTH);
            height = findDesiredDimensionInRange(LANDSCAPE_HEIGHT_RATIO, viewResolution.y, MIN_FRAME_HEIGHT, LANDSCAPE_MAX_FRAME_HEIGHT);
        } else {
            width = findDesiredDimensionInRange(PORTRAIT_WIDTH_RATIO, viewResolution.x, MIN_FRAME_WIDTH, PORTRAIT_MAX_FRAME_WIDTH);
            height = findDesiredDimensionInRange(PORTRAIT_HEIGHT_RATIO, viewResolution.y, MIN_FRAME_HEIGHT, PORTRAIT_MAX_FRAME_HEIGHT);
        }

        int leftOffset = (viewResolution.x - width) / 2;
        int topOffset = (viewResolution.y - height) / 2;
        mFramingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
    }

    private static int findDesiredDimensionInRange(float ratio, int resolution, int hardMin, int hardMax) {
        int dim = (int) (ratio * resolution);
        if (dim < hardMin) {
            return hardMin;
        }
        if (dim > hardMax) {
            return hardMax;
        }
        return dim;
    }
}