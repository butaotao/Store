package com.dachen.dgroupdoctorcompany.archive;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.dachen.dgroupdoctorcompany.R;

import io.vov.vitamio.widget.MediaController;

public class CustomMediaController extends MediaController {
	private Context mContext;
	private ImageButton mZoom;
	private boolean mIsFullScreen = false;

	public CustomMediaController(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public CustomMediaController(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	private void init(Context context) {
		mContext = context;
	}

	@Override
	protected View makeControllerView() {
		// TODO Auto-generated method stub
		View view =  ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				getResources().getIdentifier("custom_mediacontroller", "layout", mContext.getPackageName()), this);
		mZoom = (ImageButton) view.findViewById(R.id.mediacontroller_zoom);
		mZoom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mContext instanceof Activity){
					Activity activity = (Activity) mContext;
//					if(mIsFullScreen){
//						mIsFullScreen = false;
//					}else{
//						mIsFullScreen = true;
//					}
					if(isScreenOriatationPortrait(mContext)){
						activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
						mZoom.setImageResource(R.drawable.smallscreen);
					}else{
						activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
						mZoom.setImageResource(R.drawable.fullscreen);
					}
				}
			}
		});
		return view;
	}
	
	/**
	 * 返回当前屏幕是否为竖屏。
	 * 
	 * @param context
	 * @return 当且仅当当前屏幕为竖屏时返回true,否则返回false。
	 */
	private  boolean isScreenOriatationPortrait(Context context) {
		return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
	}

}
