package com.dachen.dgroupdoctorcompany.im.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dachen.common.utils.Logger;
import com.dachen.dgroupdoctorcompany.R;
import com.dachen.imsdk.ImSdk;
import com.dachen.imsdk.vchat.VChatManager;
import com.dachen.imsdk.vchat.VChatUtil;
import com.dachen.imsdk.vchat.activity.VChatActivity;

import java.lang.ref.WeakReference;
import java.util.Locale;

/**
 * Created by Mcp on 2016/3/22.
 */
public class VChatFloatService extends Service {

    private static final String TAG = "VChatFloatService";
    public static final String INTENT_ACTION="action";
    public static final int ACTION_SHOW =1;
    public static final int ACTION_CLOSE=2;

    private static final int MSG_CLOSE=101;
    private static final int MSG_SHOW=102;
    private static final int MSG_UPDATE=103;
    LinearLayout mFloatLayout;
    TextView tvTime;
    LayoutParams wmParams;
    WindowManager mWindowManager;
    private boolean mViewAdded;
    private MyHandler mHandler;

    private float mLastX;
    private float mLastY;
    private boolean hasMoved;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createFloatView();
        mHandler=new MyHandler(this);
//        Intent i=new Intent();
//        startService(i);
    }

    private void createFloatView()
    {
        wmParams = new LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        Logger.i(TAG, "mWindowManager--->" + mWindowManager);
        //设置window type
        wmParams.type = LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.RIGHT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据
        wmParams.width = LayoutParams.WRAP_CONTENT;
        wmParams.height = LayoutParams.WRAP_CONTENT;

         /*// 设置悬浮窗口长宽数据
        wmParams.width = 200;
        wmParams.height = 80;*/

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.vchat_float, null);
        tvTime= (TextView) mFloatLayout.findViewById(R.id.time);
        //添加mFloatLayout
//        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮
//        mFloatView = (Button)mFloatLayout.findViewById(R.id.float_id);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //设置监听浮动窗口的触摸移动
        mFloatLayout.setOnTouchListener(new OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    hasMoved=false;
                }
                else if(event.getAction()==MotionEvent.ACTION_MOVE){
                    float moveX=event.getRawX() - mLastX;
                    float moveY=event.getRawY()-mLastY;
                    if(!hasMoved&& (Math.max(moveX,moveY)>10||Math.min(moveX,moveY)<-10) ){
                        hasMoved=true;
                    }
                    wmParams.x=  Math.round(wmParams.x - (event.getRawX() - mLastX));
                    wmParams.y=  Math.round(wmParams.y+event.getRawY()-mLastY);
                    mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                }
                mLastX =event.getRawX();
                mLastY=event.getRawY();
//                wmParams.x = (int) event.getRawX() - mFloatLayout.getMeasuredWidth()/2;
//                wmParams.y = (int) event.getRawY() - mFloatLayout.getMeasuredHeight()/2 - 25;
//                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                return false;  //此处必须返回false，否则OnClickListener获取不到监听
            }
        });

        mFloatLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasMoved)
                    return;
                closeWindow();
                int roomId=VChatManager.getInstance().curRoomId;
                startActivity(new Intent(getApplicationContext(), VChatActivity.class)
                        .putExtra(VChatUtil.EXTRA_RELATION_ID, roomId)
                        .putExtra(VChatUtil.EXTRA_SELF_IDENTIFIER, ImSdk.getInstance().userId)
                        .putExtra("from", true)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) );
            }
        });

    }
    private void showWindow(){
        if(!mViewAdded){
            mWindowManager.addView(mFloatLayout, wmParams);
            updateTime();
            mViewAdded=true;
        }
    }

    private void closeWindow(){
        if(!mViewAdded)
            return;
        mHandler.removeMessages(MSG_UPDATE);
        mViewAdded=false;
        mWindowManager.removeView(mFloatLayout);
    }
    private void updateTime(){
        mHandler.removeMessages(MSG_UPDATE);
        long start= VChatManager.getInstance().startTime;
        if(start==0){
            tvTime.setText("00:00");
        }else {
            long timeMils=System.currentTimeMillis()-start;
            int secTotal= (int) (timeMils/1000);
            int min=secTotal/60;
            int sec=secTotal%60;
            String str=String.format(Locale.getDefault(),"%02d:%02d",min,sec);
            tvTime.setText(str);
        }
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE, 1000);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent==null)
            return super.onStartCommand(intent, flags, startId);
        int action=intent.getIntExtra(INTENT_ACTION,0);
        if(action== ACTION_SHOW){
            mHandler.removeMessages(MSG_CLOSE);
//            mHandler.obtainMessage(MSG_SHOW).sendToTarget();
            showWindow();
        }else{
            mHandler.sendEmptyMessageDelayed(MSG_CLOSE,200);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public static void startWork(Context context, int action){
        Intent i=new Intent(context,VChatFloatService.class);
        i.putExtra(INTENT_ACTION,action);
        context.startService(i);
    }

    @Override
    public void onDestroy() {
        closeWindow();
        super.onDestroy();
    }
    private static class MyHandler extends Handler{
        WeakReference<VChatFloatService> ref;

        public MyHandler( VChatFloatService service) {
            this.ref = new WeakReference<VChatFloatService>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            VChatFloatService service=ref.get();
            if(service==null)return;
            switch (msg.what){
                case MSG_CLOSE:
                    service.closeWindow();
                    break;
                case MSG_SHOW:
                    service.showWindow();
                    break;
                case MSG_UPDATE:
                    service.updateTime();
                    break;
            }
        }
    }
}
