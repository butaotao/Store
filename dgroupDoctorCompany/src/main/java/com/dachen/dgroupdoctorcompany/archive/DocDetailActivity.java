package com.dachen.dgroupdoctorcompany.archive;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;


/**
 * 文档类型文件详情
 * @author gaozhuo
 * @date 2016/2/23
 *
 */
public class DocDetailActivity extends ArchiveItemDetailUi {

    private TextView mFileName;
    private ImageView mFileType;

    @Override
    protected int getContentViewLayoutResId() {
        return R.layout.archive_content_doc_detail;
    }

    @Override
    protected void onContentViewLoaded(View v) {
        mFileName = (TextView) v.findViewById(R.id.tv_name);
        mFileName.setText(mItem.name);

        mFileType = (ImageView) v.findViewById(R.id.iv_pic);
        mFileType.setImageResource(ArchiveUtils.getFileIcon(mItem.suffix));

    }
}
