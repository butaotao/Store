package com.dachen.dgroupdoctorcompany.archive;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.dachen.dgroupdoctorcompany.R;

/**
 * 文档类型文件详情
 * @author gaozhuo
 * @date 2016/2/23
 *
 */
public class VideoDetailActivity extends ArchiveItemDetailUi {
    private static  final int REQUEST_CODE_VIDEO = 1;
    private ImageView mPlay;
    private long mPosition = 0;

    @Override
    protected int getContentViewLayoutResId() {
        return R.layout.archive_content_video_detail;
    }

    @Override
    protected void onContentViewLoaded(View v) {
        mPlay = (ImageView) v.findViewById(R.id.play);
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoDetailActivity.this, VideoPlayerActivity.class);
                intent.putExtra("videoUrl", mItem.url);
                intent.putExtra("position", mPosition);
                startActivityForResult(intent, REQUEST_CODE_VIDEO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_VIDEO && resultCode == RESULT_OK){
            if(data != null){
                mPosition = data.getLongExtra("position", 0);
            }
        }
    }
}
