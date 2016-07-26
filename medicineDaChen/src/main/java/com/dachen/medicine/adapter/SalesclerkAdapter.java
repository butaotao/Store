package com.dachen.medicine.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;

import com.dachen.medicine.R;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.CdrugRecipeitem;
import com.dachen.medicine.logic.CompareData;
import com.dachen.medicine.logic.ScaningData;

public class SalesclerkAdapter extends BaseCustomAdapter<CdrugRecipeitem> {
	List<CdrugRecipeitem> objects;
	List<CdrugRecipeitem> objectsReal;
	String isopen = "-1";
	boolean show;
	Context context;
	boolean isshowhavebuy = false;
	boolean isshowXIALA = false;
	int openPosition = -1;
	ImageView redcircleedit;
	ImageView redcircleescan;
	public SalesclerkAdapter(Context context, int resId,
			List<CdrugRecipeitem> objects,boolean show,ImageView redcircleedit,ImageView redcircleescan) {
		
		super(context, resId, objects);
		// TODO Auto-generated constructor stub
		this.objects = objects;
		openPosition = -1;
		isopen = SharedPreferenceUtil.getString("isopen_" + SharedPreferenceUtil.getString("id", ""), "1");
		this.show= show;
		this.context = context;
		for (int i = 0; i < this.objects.size(); i++) {
			 if (this.objects.get(i).manufacturer.contains("#")) {
				 this.objects.remove(i);
			} 
		}
		objectsReal = new ArrayList<CdrugRecipeitem>();
		this.redcircleedit = redcircleedit;
		this.redcircleescan =redcircleescan;
		objectsReal.addAll(this.objects);
		this.redcircleescan.setVisibility(View.GONE);
		this.redcircleedit.setVisibility(View.GONE);
		notifyDataSetChanged();
	}
	public int getItemSize(){ 
		if(objects != null )
			return objects.size();
		return 0;
	}
	public void setshow(boolean show){
		redcircleescan.setVisibility(View.GONE);
		redcircleedit.setVisibility(View.GONE);
	this.show = show;
	}
	public void setObjects(List<CdrugRecipeitem> objectsReal)  {
		List<CdrugRecipeitem> objectNew = new ArrayList<>();
		for (int i=0;i<objectsReal.size();i++){
			try {
				objectNew.add((CdrugRecipeitem) objectsReal.get(i).deepCopy());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.objects.clear();
		 for (int i = 0; i < objectNew.size(); i++) {
			 if (objectNew.get(i).manufacturer.contains("#")) {
				 continue;
			 }
			 CdrugRecipeitem item = null;
			try {
				item = (CdrugRecipeitem)objectNew.get(i).deepCopy();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.objects.add(item);
		}
		this.objectsReal.clear();
		this.objectsReal.addAll(this.objects);
		notifyDataSetChanged();
	}
	@Override
	protected com.dachen.medicine.adapter.BaseCustomAdapter.BaseViewHolder getViewHolder() {
		// TODO Auto-generated method stub
		return new ViewHolder();
	}

	@Override
	protected void fillValues(
			com.dachen.medicine.adapter.BaseCustomAdapter.BaseViewHolder baseViewHolder,
			final int position) {
		// TODO Auto-generated method stub
		final ViewHolder holder = (ViewHolder) baseViewHolder;
		final CdrugRecipeitem media = getItem(position);
		String name = ""; 
		if (isopen.equals("1")) {
			name = media.general_name;
		}else {
			name = media.trade_name;
			if (name==null||name.equals("")) {
				name = media.general_name;
			}
		}  
		if (null != name && name.length() > 9) {
			name = name.substring(0, 6) + "..."
					+ name.substring(name.length() - 2);
			 
		}
		holder.iv_circle.setVisibility(View.GONE);
		holder.tv_moreorless.setText("");
		holder.tv_name.setText(name + "");
		holder.tv_weight.setText(media.pack_specification + "");

		holder.tv_weight_unit.setText("");
		holder.tv_nums.setText(media.requires_quantity + "");
		if (null==media.unit||TextUtils.isEmpty(media.unit.name)){
			holder.tv_units.setText("");
			holder.tv_units.setVisibility(View.INVISIBLE);
		}else{
			holder.tv_units.setVisibility(View.VISIBLE);
			holder.tv_units.setText(media.unit.name + "");
		}
		holder.tv_units.setText(media.unit.name + "");
		holder.tv_companyinfo.setText(media.manufacturer + "");
		holder.tv_companyinfo.setTextColor(context.getResources().getColor(R.color.color_aaaaaa));
		holder.btn_zeng.setVisibility(View.GONE);
		holder.imageView1.setBackgroundResource(R.drawable.yaochang);

		if(CompareData.isShow(media)||media.givePresent ){
			holder.btn_zeng.setVisibility(View.VISIBLE);
		}else {
			holder.btn_zeng.setVisibility(View.GONE);
		}
	//	ToastUtils.showToast("====111==="+position);
		if (show) {   
		//	ToastUtils.showToast("======="+position);
			//media.bought_quantity 已经购买    media.requires_quantity ,media.numResone 差异

			int m = 0;
			int haveBrount = 0;
			int require = 0;
			if (0!=media.requires_quantity) {
				require= media.requires_quantity;
			}
			int requireNum =  require;
			if (0!=media.bought_quantity) {
				haveBrount =  media.bought_quantity; 
			}else { 
				haveBrount = 0;
			} 
			 
			if (-11111!=media.numResone) {
				int n = media.numResone;
				m = haveBrount+n;
				 if (m>requireNum) {
					 if (media.numResone == 0) {
						    holder.iv_circle.setVisibility(View.VISIBLE);
							holder.iv_circle.setBackgroundResource(R.drawable.circlegreen);
							holder.tv_moreorless.setText("已购买");
							holder.tv_moreorless.setTextColor(context.getResources().getColor(R.color.color_39cf78));
						}else if(media.numResone > 0){
						 holder.iv_circle.setVisibility(View.VISIBLE);
							holder.iv_circle.setBackgroundResource(R.drawable.circleblue); 
							holder.tv_moreorless.setTextColor(context.getResources().getColor(R.color.color_0084ff));
							//上次买多了，就显示总共买多的 
							if ((m- requireNum)< n) { 
								holder.tv_moreorless.setText(context.getString(R.string.more_medie) + Math.abs(m - requireNum) + media.unit.name);
								if (!SharedPreferenceUtil.getBoolean("edit_sale",false)){
									redcircleedit.setVisibility(View.VISIBLE);
								}

							}else {
								//上次没买够，那么显示n(这次选择的)
								holder.tv_moreorless.setText(context.getString(R.string.more_medie)+n+media.unit.name);
								if (!SharedPreferenceUtil.getBoolean("edit_sale",false)) {
									redcircleedit.setVisibility(View.VISIBLE);
								}
							} 
						} 
				}else if(m==requireNum){
					if (n == 0) {
						holder.iv_circle.setVisibility(View.VISIBLE);
						holder.iv_circle.setBackgroundResource(R.drawable.circlegreen);
						holder.tv_moreorless.setText("已购买");
						holder.tv_moreorless.setTextColor(context.getResources().getColor(R.color.color_39cf78));
					}else if(n > 0){
						//不多不少
						holder.iv_circle.setVisibility(View.VISIBLE);
						holder.iv_circle.setBackgroundResource(R.drawable.circlegreen);
						holder.tv_moreorless.setText(context.getString(R.string.have_scan_medie));
						holder.tv_moreorless.setTextColor(context.getResources().getColor(R.color.color_39cf78));
					}
				}else if(m<requireNum&&m>0){
					 holder.iv_circle.setVisibility(View.VISIBLE);
					holder.iv_circle.setBackgroundResource(R.drawable.point_orange);
					holder.tv_moreorless.setText(context.getString(R.string.less_medie) + (requireNum - m) + media.unit.name);
					 if (!SharedPreferenceUtil.getBoolean("scan_sale",false)) {
						 redcircleescan.setVisibility(View.VISIBLE);
					 }
					holder.tv_moreorless.setTextColor(context.getResources().getColor(R.color.color_ff8948));
				} else if(m==0){
					if (!media.is_own) {
						holder.iv_circle.setVisibility(View.VISIBLE);
						//缺货
						holder.iv_circle.setBackgroundResource(R.drawable.circlered);
						holder.tv_moreorless.setText(context.getString(R.string.short_medie));
						holder.tv_moreorless.setTextColor(context.getResources().getColor(R.color.color_fa3f3f));
					}else {
						holder.iv_circle.setVisibility(View.VISIBLE);
						//不缺货，但是没有扫描
						holder.iv_circle.setBackgroundResource(R.drawable.point_orange);
						holder.tv_moreorless.setText(context.getString(R.string.less_medie) + requireNum + media.unit.name);
						if (!SharedPreferenceUtil.getBoolean("scan_sale",false)){
							redcircleescan.setVisibility(View.VISIBLE);
						}

						holder.tv_moreorless.setTextColor(context.getResources().getColor(R.color.color_ff8948));
					}
				}
			}

			holder.ll_buy.setVisibility(View.GONE); 
			holder.rl_tab.setVisibility(View.VISIBLE);
			}else {
			redcircleescan.setVisibility(View.GONE);
			redcircleedit.setVisibility(View.GONE);
					int havebuy_num = 0;
					int require_num = 0;
					int buylot = 0;
					if (0!=media.requires_quantity) {
						require_num =  media.requires_quantity;
					}else {
						require_num = 0;
					}
					if (0!=media.bought_quantity) {
						havebuy_num =  media.bought_quantity ;
						
						
					}else { 
						havebuy_num = 0;
					}
					
					buylot = havebuy_num - require_num;
				if (!media.is_own) { 
					if (buylot>=0) {
						holder.iv_circle.setVisibility(View.VISIBLE);
						holder.iv_circle.setBackgroundResource(R.drawable.circlegreen); 
						holder.tv_moreorless.setText("已购买"); 
						if ( !TextUtils.isEmpty(media.lastDrugStoreName)) {
							holder.tv_companyinfo.setText(media.lastDrugStoreName);
							holder.imageView1.setBackgroundResource(R.drawable.yaofang);
							holder.tv_companyinfo.setTextColor(context.getResources().getColor(R.color.color_39cf78));
						}
						holder.tv_moreorless.setTextColor(context.getResources().getColor(R.color.color_39cf78));
					}else {
						holder.iv_circle.setVisibility(View.VISIBLE);
						holder.iv_circle.setBackgroundResource(R.drawable.circlered);	
						holder.tv_moreorless.setText(context.getString(R.string.short_medie));
						holder.tv_moreorless.setTextColor(context.getResources().getColor(R.color.color_fa3f3f)); 
					}
				}else { 
					if (buylot>=0&&require_num!=0) {
						holder.iv_circle.setVisibility(View.VISIBLE);
						holder.iv_circle.setBackgroundResource(R.drawable.circlegreen); 
						holder.tv_moreorless.setTextColor(context.getResources().getColor(R.color.color_39cf78));
						holder.tv_moreorless.setText("已购买" ); 
						if (!TextUtils.isEmpty(media.lastDrugStoreName)) {
							holder.tv_companyinfo.setText(media.lastDrugStoreName);
							holder.imageView1.setBackgroundResource(R.drawable.yaofang);
							holder.tv_companyinfo.setTextColor(context.getResources().getColor(R.color.color_39cf78));
						}
					}else if(buylot<0&&havebuy_num!=0){
						holder.iv_circle.setVisibility(View.VISIBLE);
						holder.iv_circle.setBackgroundResource(R.drawable.point_orange);
						holder.tv_moreorless.setText(context.getString(R.string.less_medie)+Math.abs(buylot)+media.unit.name);
						holder.tv_moreorless.setTextColor(context.getResources().getColor(R.color.color_ff8948));
					}else if(havebuy_num==0){

						holder.iv_circle.setVisibility(View.GONE);
						holder.tv_moreorless.setText("");
					}
				}
				holder.ll_buy.setVisibility(View.GONE);
				holder.rl_tab.setBackgroundResource(R.drawable.btn_bulk_greens);
				if (position==0) { 
						holder.ll_buy.setVisibility(View.VISIBLE);
					if(ScaningData.sCanFlag == 4||ScaningData.sCanFlag == 5){
						holder.tv_shownum.setText("以下为待赠送的药品");
					}else{
						holder.tv_shownum.setText("以下为待购买药品");
					}

						holder.iv_open.setVisibility(View.GONE);
				}
				if (holder.tv_moreorless.getText().toString().contains("已购买")) {
					
					if (!isshowXIALA||openPosition==position) {
						openPosition=position;
						holder.iv_open.setVisibility(View.VISIBLE);
						isshowXIALA = true;
						holder.ll_buy.setVisibility(View.VISIBLE);  
						holder.tv_shownum.setText("展开已购买药品("+(objectsReal.size()-position)+")");
						
					}
					//holder.ll_buy.setVisibility(View.VISIBLE);
					if (isshowhavebuy) {
						holder.tv_shownum.setText("隐藏已购买药品("+(objectsReal.size()-position)+")");
						holder.rl_tab.setVisibility(View.VISIBLE);
						holder.iv_open.setBackgroundResource(R.drawable.list_closed);
					}else {

						holder.tv_shownum.setText("展开已购买药品("+(objectsReal.size()-position)+")");
						holder.rl_tab.setBackgroundResource(R.color.white);
						holder.rl_tab.setVisibility(View.GONE);
						holder.iv_open.setBackgroundResource(R.drawable.list_open);
					}

					holder.ll_buy.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							isshowXIALA = false;
							if (isshowhavebuy) {
								isshowhavebuy = false;
							} else {
								isshowhavebuy = true;
							}

							if (!isshowhavebuy) {

								int num = position;
								for (int i = position; i < objects.size(); i++) {
									if (i == position) {
										holder.rl_tab.setVisibility(View.GONE);
									} else {
										holder.rl_tab.setVisibility(View.VISIBLE);
										objects.remove(i);
										notifyDataSetChanged();
									}

								}
							} else {

								objects.clear();
								objects.addAll(objectsReal);
								notifyDataSetChanged();

							}
							if (isshowhavebuy) {

								holder.iv_open.setBackgroundResource(R.drawable.list_closed);
							}else {
								holder.iv_open.setBackgroundResource(R.drawable.list_open);
							}
						}

					});
					 
				
			}
	 
	 	if (position % 2 == 0) {
			holder.miden_line.setBackgroundResource(R.color.color_greenline1dp);
		} else {
			holder.miden_line.setBackgroundResource(R.color.color_grayline1dp);
		} 
	}
	}

	public static class ViewHolder extends BaseViewHolder {
		@Bind(R.id.tv_name)
		TextView tv_name;
		@Bind(R.id.tv_weight)
		TextView tv_weight;
		@Bind(R.id.tv_units)
		TextView tv_units;
		@Bind(R.id.tv_weight_unit)
		TextView tv_weight_unit;
		@Bind(R.id.tv_nums)
		TextView tv_nums;
		@Bind(R.id.tv_companyinfo)
		TextView tv_companyinfo;
		@Bind(R.id.view_lastline)
		View view_lastline;
		@Bind(R.id.miden_line)
		View miden_line;
		@Bind(R.id.iv_circle)
		ImageView iv_circle;
		@Bind(R.id.tv_moreorless)
		TextView tv_moreorless;
		@Bind(R.id.ll_buy)
		LinearLayout ll_buy;
		@Bind(R.id.rl_tab)
		RelativeLayout rl_tab;
		@Bind(R.id.iv_open)
		ImageView iv_open;
		@Bind(R.id.tv_shownum)
		TextView tv_shownum;
		@Bind(R.id.rl_tab_all)
		RelativeLayout rl_tab_all;
		//是否赠药
		@Bind(R.id.btn_zeng)
		TextView btn_zeng;
		@Bind(R.id.imageView1)
		ImageView imageView1;
	}
}
