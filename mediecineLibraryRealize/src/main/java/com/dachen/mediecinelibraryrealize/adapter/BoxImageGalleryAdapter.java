package com.dachen.mediecinelibraryrealize.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.ImageUtil;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.net.CustomImagerLoader;
import com.dachen.mediecinelibraryrealize.R;
import com.dachen.mediecinelibraryrealize.utils.AlarmData;

import java.util.ArrayList;

public class BoxImageGalleryAdapter extends BaseAdapter {

	private Context context;
	 private int selectItem;
	ArrayList<AlarmData> patients;
	// 里面所有的方法表示的是可以根据指定的显示图片的数量,进行每个图片的处理
	  Animation animation  ;    //实现动画效果
	Animation animationSmall;
	public BoxImageGalleryAdapter(Context context) {
		this.context = context;
		 this.animation = AnimationUtils.loadAnimation(this.context, R.anim.my_scale_action);    //实现动画效果  ;
		this.animationSmall = AnimationUtils.loadAnimation(this.context, R.anim.my_scalebigaction);    //实现动画效果  ;

	}
	public BoxImageGalleryAdapter(Context context, ArrayList<AlarmData> patients) {
		this.patients = patients;
		this.context = context;
		 this.animation = AnimationUtils.loadAnimation(this.context, R.anim.my_scale_action);    //实现动画效果  ;
		this.animationSmall = AnimationUtils.loadAnimation(this.context, R.anim.my_scalebigaction);    //实现动画效果  ;
	}

	public int getCount() { // 取得要显示内容的数量

		return patients.size();

	}

	public Object getItem(int position) { // 每个资源的位置

		return patients.get(position);

	}

	public long getItemId(int position) { // 取得每个项的ID

		return  position ;

	}
	public void setSelectItem(int selectItem) {  
		           
		        if (this.selectItem != selectItem) {                  
		         this.selectItem = selectItem;
		       notifyDataSetChanged();                 
		        }  
		   }  

	// 将资源设置到一个组件之中，很明显这个组件是ImageView
 
	public View getView(int position, View convertView, ViewGroup parent) {
		  ViewHolder holder;
		AlarmData p = (AlarmData) getItem(position);
		            if(convertView == null){  
		                 //View的findViewById()方法也是比较耗时的，因此需要考虑中调用一次，之后用  
		                //View的getTag()来获取这个ViewHolder对象  
		                holder = new ViewHolder();  
		                    convertView = View.inflate(context, R.layout.adapter_imagegallery, null);  
		                 holder.imageView = (ImageView) convertView.findViewById(R.id.image);  
		                 holder.textView = (TextView) convertView.findViewById(R.id.text);   
		               convertView.setTag(holder);  
		            }else {  
		               holder = (ViewHolder) convertView.getTag();  
		              }  
		            if (null!=p.logo) {
						String ip = UserInfo.getInstance(context).getIP().get("ipnotport");
						String session = UserInfo.getInstance(context).getSesstion();
						String urls = ImageUtil.getUrl(p.logo, ip, session, 200);
		            	CustomImagerLoader.getInstance().loadImage(holder.imageView, urls,R.drawable.head_icon,R.drawable.head_icon) ;
					}
		            
		              holder.textView.setText(p.patientName);
 
		                   float weight = context.getResources().getDimension(R.dimen.galleryweight);
	                      //给生成的ImageView设置Id，不设置的话Id都是-1    
	                         if(selectItem==position){
							/*	 Animation scaleAnimation = new ScaleAnimation(0.8f, 1.0f,0.8f,1.0f);
								  //设置动画时间
								 scaleAnimation.setDuration(500);
								 holder.imageView.startAnimation(scaleAnimation);*/
								float w = context.getResources().getDimension(R.dimen.galleryimagewidth);
								 holder.imageView.setLayoutParams(new LinearLayout.LayoutParams((int) (weight + w), (int) (weight + w)));
	                        	  holder.imageView.startAnimation(animation);  //选中时，这是设置的比较大  
	                        	  holder.imageView.setBackgroundResource(R.drawable.btn_bulk_63aeea);
	                         }else {
	                        	
	                        	 holder.imageView.setLayoutParams(new LinearLayout.LayoutParams((int) (weight), (int) (weight)));  

								 holder.imageView.startAnimation(animationSmall);
	                        	  holder.imageView.setBackgroundResource(R.drawable.btn_bulk_d6d6d6);
							}
		                    
	                        // holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);    
	                        return convertView;   


	}
	   final class ViewHolder{  
		       public ImageView imageView;  
		         public TextView textView;   
	   }  

}