package com.dachen.dgroupdoctorcompany.views;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.app.CompanyApplication;

/**
 * Created by Burt on 2016/2/27.
 */
public class ImageToast {
    public static void showImageToast(Context context){
        Toast toast;
        toast = Toast.makeText(context,
                "带图片的Toast", Toast.LENGTH_LONG);
        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView imageCodeProject = new ImageView(context);
        imageCodeProject.setImageResource(R.drawable.ic_default_head);
        toastView.addView(imageCodeProject, 0);
        toast.setGravity(Gravity.FILL | Gravity.CENTER, 0, 200);

        toast.show();
    }
    public static void makeToast(Context context) {
        View toastView;
            LayoutInflater inflater = (LayoutInflater) CompanyApplication.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            toastView = inflater.inflate(R.layout.toast_addimage, null);

        //TextView tv = (TextView) toastView.findViewById(R.id.tv_title);
        //tv.setLayoutParams(params);
        //tv.setText(msg);

        Toast toast = new Toast(CompanyApplication.getInstance());
        float hOffset = 200;
        toast.setGravity(Gravity.FILL , 0, (int) hOffset);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastView);
        toast.show();
    }

}
