package com.dachen.mediecinelibraryrealize.entity; 
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;

/**
 * 
 * @author gaozhuo
 * @date 2015年12月2日
 *
 */
public class SoundPlayer {
	private Context mContext;
	public static MediaPlayer mMediaPlayer;

	public SoundPlayer(Context context) {
		mContext = context;
		initMediaPlayer();
	}
	
	private void initMediaPlayer(){
		if (null==mMediaPlayer){
			mMediaPlayer = new MediaPlayer();
		}

		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
		mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mp.start();
			}
		});
	}

	public void play(Uri uri) {
		if (mMediaPlayer == null) {
			return;
		}
		mMediaPlayer.reset();
		try {
			mMediaPlayer.setDataSource(mContext, uri);
			mMediaPlayer.prepareAsync();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stop() {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
		}
	}

	public void release() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}
	public boolean isPlaying(){
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			return true;
		}
		return false;
	}

}
