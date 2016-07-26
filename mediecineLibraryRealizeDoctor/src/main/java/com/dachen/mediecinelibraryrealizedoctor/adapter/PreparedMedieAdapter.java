package com.dachen.mediecinelibraryrealizedoctor.adapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.ImageUtil;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.MedicineApplication;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.net.CustomImagerLoader;
import com.dachen.medicine.view.AddAndSubView;
import com.dachen.medicine.view.AddAndSubView.OnNumChangeListener;
import com.dachen.mediecinelibraryrealizedoctor.R;
import com.dachen.mediecinelibraryrealizedoctor.activity.GetMedieDetaiInfo;
import com.dachen.mediecinelibraryrealizedoctor.activity.MedieSpecificationActivity;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.utils.CompareDatalogic;
import com.dachen.mediecinelibraryrealizedoctor.utils.StringUtils;

public class PreparedMedieAdapter extends BaseAdapter {
	AddAndSubView addAndSubView1;
	Activity context;
	public List<MedicineInfo> list; 
	ChoiceItemNumInterface numChoices;
	int number;
	protected LayoutInflater mInflater; 
	 ViewHolder holder ;
	 //1为最近所选，2为选药首页（可以进入最近等），3为创建药单
	 int isclick = 0;
	 HashMap<String, String> maps ;
		 int clickPosition;
	public PreparedMedieAdapter(Activity context,
			  List<MedicineInfo> objects, 
			ChoiceItemNumInterface numChoice) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		list = objects;
		numChoices = numChoice;  
		 maps = MedicineApplication.getMapConfig();
		// TODO Auto-generated constructor stub
	}  
	public PreparedMedieAdapter(Activity context,
			  List<MedicineInfo> objects, 
			ChoiceItemNumInterface numChoice,int isclick) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		list = objects;
		numChoices = numChoice;  
		 maps = MedicineApplication.getMapConfig();
		 this.isclick = isclick;
		// TODO Auto-generated constructor stub
	} 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		
		View v = convertView; 
		  if (v == null) {
			  v = mInflater.inflate(R.layout.adapter_preparemedie, parent, false); 
			  holder = new ViewHolder();  
			  holder.img_listview = (ImageView) v.findViewById(R.id.img_listview);
			  holder.tv_medicine_name = (TextView) v.findViewById(R.id.tv_medicine_name);
			  holder.tv_medicine_height = (TextView) v.findViewById(R.id.tv_medicine_height);
			  holder.tv_company = (TextView) v.findViewById(R.id.tv_company);
			  holder.ll_numshow = (RelativeLayout) v.findViewById(R.id.ll_numshow);
			  holder.subview_add = (AddAndSubView) v.findViewById(R.id.subview_add); 
			  holder.ll_childern = (RelativeLayout) v.findViewById(R.id.ll_childern); 
			  holder.iv_single = (RelativeLayout) v.findViewById(R.id.iv_single);
			  holder.iv_add_single = (ImageView) v.findViewById(R.id.iv_add_single);
			  holder.ll_diviver = (LinearLayout) v.findViewById(R.id.ll_diviver);
			  holder.rl_medicinename = (RelativeLayout)v.findViewById(R.id.rl_medicinename);
			  holder.ll_numshow_text = (LinearLayout) v.findViewById(R.id.ll_numshow_text);
			  holder.tv_packages = (TextView) v.findViewById(R.id.tv_packages);
			  holder.iv_company = (ImageView) v.findViewById(R.id.iv_company);
			  holder.iv_pared = (ImageView) v.findViewById(R.id.iv_pared);
			  holder.img_company=(ImageView) v.findViewById(R.id.img_company);
			  v.setTag(holder); 
		  }else {
			holder = (ViewHolder) v.getTag();
		  }

			 holder.ll_diviver.setVisibility(View.GONE);   
			holder.ll_childern.setVisibility(View.VISIBLE);
			final MedicineInfo media = (MedicineInfo) getItem(position);
		//	LogUtils.burtLog("==position====="+position+"=="+media);
		if (media.is_group_goods&& UserInfo.getInstance(context).getUserType().equals("3") ) {
			holder.img_company.setVisibility(View.VISIBLE);
		}else {
			holder.img_company.setVisibility(View.GONE);
		}
			holder.tv_packages.setText(media.goods$pack_specification);
			//goods$general_name
			/*if (null==name) {
				name = media.goods$trade_name;
			}if (null != name && name.length() > 9) {
				name = name.substring(0, 6) + "..."
						+ name.substring(name.length() - 2); 
			}  
			 if (TextUtils.isEmpty(name)&&!TextUtils.isEmpty(media.trade_name)) {
				 if (media.trade_name.length()>9) {
					 name =  media.trade_name.substring(0, 6) + "...";
				}else {
					 name =  media.trade_name;
				}
				
			}*/
		String goodsname = "";
			goodsname = media.title;
	 	String names = CompareDatalogic.getShowName2(media.goods$general_name, media.goods$trade_name, goodsname,
				holder.tv_medicine_name, media.goods$specification,context);
			holder.tv_medicine_name.setText(names);
		//StringUtils.get(holder.tv_medicine_name, goodsname, media.goods$specification, context);
			holder.tv_medicine_height.setText(media.goods$specification);
	float size =	getTextWidth(context,goodsname,16);
			if (TextUtils.isEmpty(media.goods$manufacturer)) {
				
				/*holder.ll_childern.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
					Intent intent = new Intent(context,MedicienSearchListActivity.class);
					if (!TextUtils.isEmpty(media.trade_name)) {
						intent.putExtra("trade_name", media.trade_name);
						//context.startActivity(intent);
						context.startActivityForResult(intent, 2);
					}
				
					}
				});*/
			}
			
			holder.tv_company.setText(media.goods$manufacturer);

		String url = media.goods$image;
		if(TextUtils.isEmpty(url)){
			url = media.goods$image_url;
		}
		final GetMedieDetaiInfo info = new GetMedieDetaiInfo(context,(MedicineInfo)getItem(position));
		holder.rl_medicinename.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				info.ShowDialog();
			}
		});
		final String finalUrl = url;
		holder.img_listview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//ToastUtils.showToast(context, finalUrl);
				info.ShowDialog();
				// startMedieSpecification(context,(MedicineInfo)getItem(position));
			}
		});
		holder.ll_numshow_text.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				info.ShowDialog();
				//startMedieSpecification(context,(MedicineInfo)getItem(position));
			}
		});


			if (!TextUtils.isEmpty(url)) {
				String ip = maps.get("ip");
				String urls = ip+ImageUtil.getUrl("")+url+"?a="+maps.get("session");

				urls = ImageUtil.getUrl(url,ip,maps.get("session"),1);
				CustomImagerLoader.getInstance().loadImage(holder.img_listview,
						urls); 
			} else {
				holder.img_listview
						.setImageResource(R.drawable.image_download_fail_icon);

			}
			 
			holder.subview_add.setButtonLayoutParm(34,34);
			holder.subview_add.setButtonBgResource(R.drawable.imports,R.drawable.addpress);
			 if (null != holder.ll_numshow) {
				if (media.getNum()>0) {
					holder.subview_add.setSubVisiAble();
					holder.iv_single.setVisibility(View.INVISIBLE);
					holder.subview_add.setVisibility(View.VISIBLE); 
		 
				holder.subview_add.setNum(media.getNum()); 
				holder.subview_add.setOnNumChangeListener(new OnNumChangeListener() {
					@Override
					public void onNumChange(View view, int num) { 
						//LogUtils.burtLog("num=="+num);
						MedicineInfo entity  = (MedicineInfo) getItem(position);
						if (null != entity) {

							if (entity.num >= 0) {
								entity.num = (num ); 
								list.set(position, entity);
							}

						} else {
							entity = list.get(position);
							entity.time = (com.dachen.medicine.common.utils.TimeUtils
									.getNowTime());
							entity.num = num; 
							list.set(position, entity);
						}
						holder.subview_add.setNum(entity.num); 
						numChoices.getList(list);
					if (num>0) {
						holder.iv_single.setVisibility(View.INVISIBLE);
						holder.subview_add.setVisibility(View.VISIBLE);
						holder.subview_add.setSubVisiAble();
				    }else {
				    	holder.iv_single.setVisibility(View.VISIBLE);
						//holder.subview_add.setVisibility(View.INVISIBLE);
				    	holder.subview_add.setSubInvisiAble();
					}
				
					notifyDataSetChanged();
					}
				});
				
				}else {
					holder.subview_add.setSubInvisiAble(); 
					holder.subview_add.setOnNumChangeListener(new OnNumChangeListener() {
						@Override
						public void onNumChange(View view, int num) { 
							MedicineInfo entity = (MedicineInfo) getItem(position);
							if (null != entity) {

								if (entity.num >= 0) {
									entity.num = (1 );
									list.set(position, entity);
								}

							} else {
								entity = list.get(position);
								entity.time = (com.dachen.medicine.common.utils.TimeUtils
										.getNowTime());
								entity.num = 1; 
								list.set(position, entity);
							}
							holder.subview_add.setNum(entity.num); 
							numChoices.getList(list);
							holder.iv_single.setVisibility(View.GONE);
							holder.subview_add.setSubInvisiAble();
							
							notifyDataSetChanged();
						}
					}); 
				}	  
		} 
			if (null==media.goods$manufacturer) {
				holder.subview_add.setVisibility(View.GONE);
			
			}else {
				holder.subview_add.setVisibility(View.VISIBLE);
			}
			//holder.iv_company.setVisibility(View.GONE);
			if (isclick==2) {
				holder.iv_pared.setVisibility(View.GONE);
				/*if (!TextUtils.isEmpty(media.trade_name)) {
					holder.iv_company.setVisibility(View.VISIBLE);
				}*/
			}
			String  type= UserInfo.getInstance(context).getUserType();
			if (isclick==1||isclick==3||!type.equals("3")) {
				holder.iv_pared.setVisibility(View.GONE);
				//holder.img_company.setVisibility(View.GONE);
			}
			
			
		return v;

	}
 

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size() ;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0); 
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}


	public static class ViewHolder  { 
		ImageView img_listview; 
		TextView tv_medicine_name; 
		TextView tv_medicine_height; 
		TextView tv_company; 
		RelativeLayout ll_numshow; 
		AddAndSubView subview_add;  
		//PARENT 
		RelativeLayout ll_childern;
		RelativeLayout iv_single;
		ImageView iv_add_single;
		LinearLayout ll_diviver;
		RelativeLayout rl_medicinename;
		LinearLayout ll_numshow_text;
		TextView tv_packages;
		ImageView iv_company;
		ImageView iv_pared;
		ImageView img_company;
	}
	public void setNotfiDataSetChanged2(List<MedicineInfo> list) {
		// TODO Auto-generated method stub
		this.list = list;
		 notifyDataSetChanged();
	}
	public float getTextWidth(Context context, String text, int textSize){
		TextPaint paint = new TextPaint();
		float scaledDensity =  context.getResources().getDisplayMetrics().scaledDensity;
		paint.setTextSize(scaledDensity * textSize);
		return paint.measureText(text);
	}
}
