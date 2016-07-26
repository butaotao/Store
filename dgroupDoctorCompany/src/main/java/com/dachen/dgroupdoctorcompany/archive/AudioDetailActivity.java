package com.dachen.dgroupdoctorcompany.archive;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;

import java.io.IOException;

/**
 * 文档类型文件详情
 * @author gaozhuo
 * @date 2016/2/23
 *
 */
public class AudioDetailActivity extends ArchiveItemDetailUi {
    private static final int MSG_WHAT_UPDATE_TIME = 1;
    private TextView mFileName;
    private ImageView mFileType;
    private ImageButton mPlay;
    private TextView mTotalTime;
    private TextView mCurrentTime;
    private SeekBar mSeekBar;
    private MediaPlayer mMediaPlayer;
    private boolean mIsPrepared = false;



    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_WHAT_UPDATE_TIME:
                    update();
                    mHandler.sendEmptyMessageDelayed(MSG_WHAT_UPDATE_TIME, 1000);
                    break;

            }
        }
    };



    @Override
    protected int getContentViewLayoutResId() {
        return R.layout.archive_content_audio_detail;
    }

    @Override
    protected void onContentViewLoaded(View v) {

        initView(v);

        initMedia();

    }

    private void update() {
        mCurrentTime.setText(toTime(mMediaPlayer.getCurrentPosition()));
        mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
    }


    private void initMedia() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mIsPrepared = true;
                mTotalTime.setText(toTime(mp.getDuration()));
                mSeekBar.setMax(mp.getDuration());
                mPlay.setEnabled(true);
                mSeekBar.setEnabled(true);
            }
        });

        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                findViewById(R.id.error_tips).setVisibility(View.VISIBLE);
                return false;
            }
        });

        try {
            mMediaPlayer.setDataSource(mItem.url);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView(View v) {

        mFileName = (TextView) v.findViewById(R.id.tv_name);
        mFileName.setText(mItem.name);
        mFileType = (ImageView) v.findViewById(R.id.iv_pic);
        mFileType.setImageResource(ArchiveUtils.getFileIcon(mItem.suffix));

        mPlay = (ImageButton) v.findViewById(R.id.mediacontroller_play_pause);
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });

        mTotalTime = (TextView) v.findViewById(R.id.mediacontroller_time_total);
        mCurrentTime = (TextView) v.findViewById(R.id.mediacontroller_time_current);
        mSeekBar = (SeekBar) v.findViewById(R.id.mediacontroller_seekbar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                if (fromUser) {
                    mMediaPlayer.seekTo(progress);
                }
            }
        });

        mPlay.setEnabled(false);
        mSeekBar.setEnabled(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(MSG_WHAT_UPDATE_TIME);
        if(mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void play() {
        Log.d("gaozhuo", "mIsPrepared=" + mIsPrepared);
        if (mMediaPlayer == null || !mIsPrepared) {
            return;
        }
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPlay.setImageResource(R.drawable.mediacontroller_play);
            mHandler.removeMessages(MSG_WHAT_UPDATE_TIME);
        } else {
            mMediaPlayer.start();
            mPlay.setImageResource(R.drawable.mediacontroller_pause);
            Message msg =  mHandler.obtainMessage();
            msg.what = MSG_WHAT_UPDATE_TIME;
            msg.sendToTarget();
        }
    }

    private String toTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }
}
