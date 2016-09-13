package com.dachen.dgroupdoctorcompany.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;
import com.dachen.dgroupdoctorcompany.entity.VisitPeople;
import com.dachen.dgroupdoctorcompany.utils.CommonUitls;
import com.nostra13.universalimageloader.core.ImageLoader;

//创建一个text和Icon组合的view子类
//继承与LinerLayout表示两个IconTextView中的TextView和ImageView之间是线性布局关系。
public class IconTextView extends LinearLayout {
    //类中的两个私有成员，Text和Icon
    //IconifiedTextView中的两个子View。由TextView和ImageView组合成一个新的View。
    private TextView mText;
    private ImageView mIcon;
    public String visit_people_id;
    private float disX;//位置X
    private float disY;//位置Y
    private float angle;//旋转的角度
    private float proportion;//根据远近距离的不同计算得到的应该占的半径比例
    public float getProportion() {
        return proportion;
    }

    public void setProportion(float proportion) {
        this.proportion = proportion;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getDisX() {
        return disX;
    }

    public void setDisX(float disX) {
        this.disX = disX;
    }

    public float getDisY() {
        return disY;
    }

    public void setDisY(float disY) {
        this.disY = disY;
    }
    public IconTextView(Context context, VisitPeople visitPeople) {
        super(context);
        /* First Icon and the Text to the right (horizontal),
         * not above and below (vertical) */
        //设置IconifiedTextView为水平线性布局
        this.setOrientation(VERTICAL);
        //垂直居中对齐
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.visit_people_id = visitPeople.id;
        //添加ImageView
        mIcon = new ImageView(context);
        String picture = visitPeople.headPic;
        if(!TextUtils.isEmpty(picture)){
            ImageLoader.getInstance().displayImage(picture,mIcon, CompanyApplication.mAvatarCircleImageOptions);
        }else{
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head_icon);
            Bitmap circleBitMap = CommonUitls.getRoundedCornerBitmap(bitmap);
            mIcon.setImageBitmap(circleBitMap);
        }
        // left, top, right, bottom
        mIcon.setPadding(0, 2, 5, 2); // 5px to the right
        /* At first, add the Icon to ourself
         * (! we are extending LinearLayout) */
//        addView(mIcon,  new LinearLayout.LayoutParams(
//                LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        int mWight = (int)CommonUitls.dpToPixel(64,getContext());
        addView(mIcon,  new LinearLayout.LayoutParams(
                mWight,mWight));
        //添加TextView， 由于先添加ImageView后添加TextView，所以 ICON 在前 Text 在后。
        mText = new TextView(context);
        mText.setText(visitPeople.name);
        /* Now the text (after the icon) */
        mText.setTextSize(14);
        mText.setPadding(5,2,2,0);
        mText.setTextColor(getResources().getColor(R.color.black_333333));
        mText.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        params.gravity = Gravity.CENTER;
        addView(mText, params);
    }
    //设置TextView的Text内容
    public void setText(String words) {
        //Log.i("test","setText  "+words);
        mText.setText(words);
    }
    //设置ImageView的Icon图标
    public void setIcon(Drawable bullet) {
        //Log.i("test","setIcon  ");
        mIcon.setImageDrawable(bullet);
    }
}
