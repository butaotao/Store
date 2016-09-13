package com.dachen.dgroupdoctorcompany.adapter;

/**
 * Created by Burt on 2016/3/3.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.entity.CircleFriender;
import com.dachen.medicine.net.CustomImagerLoader;

import java.util.ArrayList;
import java.util.List;

public class CircleFriendsAdapter extends BaseAdapter<CircleFriender> implements SectionIndexer {

    private boolean isCreateGroup;
    private List<String> hashset;
    private ArrayList<String> userIds;


    public CircleFriendsAdapter(Context mContext) {
        super(mContext);
    }

    public CircleFriendsAdapter(Context mContext, boolean isCreateGroup) {
        super(mContext);
        this.isCreateGroup = isCreateGroup;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvLetter;
        TextView tv_desc;
        View view_line;
        ImageView img;
        RadioButton btn_radio;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.circle_friends_item, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            viewHolder.img = (ImageView) view.findViewById(R.id.img);
            viewHolder.view_line = (View) view.findViewById(R.id.view_line);
            viewHolder.btn_radio = (RadioButton) view.findViewById(R.id.btn_radio);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final CircleFriender mContent = dataSet.get(position);
        if (position == 0) {
            viewHolder.view_line.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getLetter());
        } else {
            String lastCatalog = dataSet.get(position - 1).getLetter();
            if (mContent.getLetter().equals(lastCatalog)) {
                viewHolder.tvLetter.setVisibility(View.GONE);
                viewHolder.view_line.setVisibility(View.GONE);
            } else {
                viewHolder.tvLetter.setVisibility(View.VISIBLE);
                viewHolder.view_line.setVisibility(View.VISIBLE);
                viewHolder.tvLetter.setText(mContent.getLetter());
            }
        }
        viewHolder.tvTitle.setText(mContent.getName());
        viewHolder.tv_desc.setText(mContent.getTitle());

        if(isCreateGroup){
            viewHolder.btn_radio.setVisibility(View.VISIBLE);
        }

        if(hashset != null && hashset.contains(mContent.getUserId())){
            viewHolder.btn_radio.setChecked(true);
        }else{
            viewHolder.btn_radio.setChecked(false);
        }

        if(mContent.isExits()){
            viewHolder.btn_radio.setEnabled(false);
        }else{
            viewHolder.btn_radio.setEnabled(true);
        }

//        ImageLoader.getInstance().displayImage(mContent.getHeadPicFileName(), viewHolder.img, CommonUitls.getHeadOptions());
        CustomImagerLoader.getInstance().loadImage(viewHolder.img, mContent.getHeadPicFileName(),
                R.drawable.ic_default_head, R.drawable.ic_default_head);

        return view;
    }


    public Object[] getSections() {
        return null;
    }

    public int getSectionForPosition(int position) {
        return 0;
    }

    public int getPositionForSection(int section) {
        CircleFriender mContent;
        String l;
        if (section == '!') {
            return 0;
        } else {
            for (int i = 0; i < getCount(); i++) {
                mContent = (CircleFriender) dataSet.get(i);
                l = mContent.getLetter();
                char firstChar = l.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i + 1;
                }

            }
        }
        mContent = null;
        l = null;
        return -1;
    }

    public List<String> getHashset() {
        return hashset;
    }

    public void setHashset(List<String> hashset) {
        this.hashset = hashset;
    }

    public ArrayList<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(ArrayList<String> userIds) {
        this.userIds = userIds;
    }

}
