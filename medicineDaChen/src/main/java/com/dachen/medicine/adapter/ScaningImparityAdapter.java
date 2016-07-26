package com.dachen.medicine.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;

import com.dachen.medicine.R;
import com.dachen.medicine.bean.MedieNum;
import com.dachen.medicine.common.utils.CustomUtils;
import com.dachen.medicine.common.utils.LogUtils;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.entity.CdrugRecipeitem;
import com.dachen.medicine.entity.DeleteInterace;
import com.dachen.medicine.entity.SelectListInterface;
import com.dachen.medicine.logic.CompareData;

public class ScaningImparityAdapter extends BaseCustomAdapter<CdrugRecipeitem>{ 
		Context context;
		SelectListInterface iterface;
		List<CdrugRecipeitem> list;
		String isopen;
	public ScaningImparityAdapter(Context context, int resId, List<CdrugRecipeitem> list,SelectListInterface iteminterface) {
		super(context, resId, list);
		this.context = context;
		this.iterface = iteminterface;
		this.list = list;
		isopen = SharedPreferenceUtil.getString("isopen_" + SharedPreferenceUtil.getString("id", ""), "1");
		 for (int i = 0; i < this.list.size(); i++) {
			 if (this.list.get(i).id.contains("#")) {
				this.list.remove(i);
			} 
		}
		// TODO Auto-generated constructor stub
	}
	@Override
	protected com.dachen.medicine.adapter.BaseCustomAdapter.BaseViewHolder getViewHolder() {
		// TODO Auto-generated method stub
		return new ViewHolder();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillValues(
			com.dachen.medicine.adapter.BaseCustomAdapter.BaseViewHolder baseViewHolder,
			final int position) {
		// TODO Auto-generated method stub
		final CdrugRecipeitem medie = list.get(position);
		final ViewHolder holder = (ViewHolder) baseViewHolder;
		holder.tv_company.setText(medie.manufacturer+"");
		String name = medie.general_name;
		if ("1".equals(isopen)) { 
			 name = medie.general_name;
		}else{ 
			 name = medie.trade_name;
			 if (name==null||name.equals("")) {
				 name = medie.general_name;
			}
		}
		if (null != name && name.length() > 9) {
			name = name.substring(0, 6) + "..."
					+ name.substring(name.length() - 2); 
		} 
		

		
		 int m = 0;
		 int n = 0;
		int haveBuy = 0;
		int require = 0; 
		if (0!=medie.bought_quantity) {
			haveBuy = medie.bought_quantity;
		}
		if (medie.numResone == -11111) {
			n = 0;
		}else {
			n = medie.numResone ;
		}
		if (0!=medie.requires_quantity) {
			require = medie.requires_quantity;
		}
		m = n + haveBuy;
		int showNumLess = 0;
		if (m<require&&m>0) {
			showNumLess = require - m;
		}else if(m==0){
			showNumLess = require - m;
		}
		
		/*•当M>X且N>0： 
		顾客增量：标记原因为“顾客增量”
		删除多余的药监码：不做异常标记
		
		•当M<X且M>0： 
		顾客减量：标记原因为“顾客减量”
		缺货：标记原因为“缺货”
		
		•当M=0： 
		无药监码：设置M=X，标记原因为“无药监码”
		顾客减量：标记原因为“顾客减量”
		缺货：标记原因为“缺货”*/

		holder.tv_name.setText(name);
		holder.tv_num.setText(medie.requires_quantity+"");
		holder.tv_unit.setText(medie.unit.name);
		holder.tv_weight.setText(medie.pack_specification); 
		holder.tv_nummore.setText("");

		holder.tv_notcode.setBackgroundResource(R.drawable.bulk_gray);
		holder.tv_reduce.setBackgroundResource(R.drawable.bulk_gray);
		holder.tv_short.setBackgroundResource(R.drawable.bulk_gray);
		holder.tv_plus.setBackgroundResource(R.drawable.bulk_gray);



		holder.tv_notcode.setTextColor(context.getResources().getColor(R.color.color_333333));
		holder.tv_reduce.setTextColor(context.getResources().getColor(R.color.color_333333));
		holder.tv_short.setTextColor(context.getResources().getColor(R.color.color_333333));
		holder.tv_plus.setTextColor(context.getResources().getColor(R.color.color_333333));
		//}


		if (medie.isSlected.equals("-1")) { 
			holder.tv_plus.setBackgroundResource(R.drawable.btn_bulk_gray);
			holder.tv_plus.setTextColor(context.getResources().getColor(R.color.color_333333)); 
		}else if(medie.isSlected.equals("7")){ 
			holder.tv_plus.setBackgroundResource(R.drawable.btn_bulk_green);
			holder.tv_plus.setTextColor(context.getResources().getColor(R.color.color_greenline2dp)); 
		}
		
		
		//if (!medie.isSlected.equals("1")&&!medie.isSlected.equals("2")&&!medie.isSlected.equals("3")&&!medie.isSlected.equals("7")) {

		if (medie.isSlected.equals("1")){
			holder.tv_notcode.setBackgroundResource(R.drawable.bulk_green);
			holder.tv_notcode.setTextColor(context.getResources().getColor(R.color.color_greenline2dp));
		}
		if (medie.isSlected.equals("2")){
			holder.tv_reduce.setBackgroundResource(R.drawable.bulk_green);
			holder.tv_reduce.setTextColor(context.getResources().getColor(R.color.color_greenline2dp));
		}
		if (medie.isSlected.equals("3")){
			holder.tv_short.setBackgroundResource(R.drawable.bulk_green);
			holder.tv_short.setTextColor(context.getResources().getColor(R.color.color_greenline2dp));
		}
		if(medie.isSlected.equals("7")){
			holder.tv_plus.setBackgroundResource(R.drawable.btn_bulk_green);
			holder.tv_plus.setTextColor(context.getResources().getColor(R.color.color_greenline2dp));
		}
		if (m<require) {
			 
			if (m==0) {
				holder.ll_not_nomber.setVisibility(View.VISIBLE); 
			}else if(m<require&&m>0){
				holder.ll_not_nomber.setVisibility(View.GONE);
			}
			 
			holder.ll_buy_des.setVisibility(View.VISIBLE);
			holder.ll_buy_des2.setVisibility(View.GONE);
			holder.tv_nummore.setText("少"+showNumLess+medie.unit.name);
			holder.tv_nummore.setTextColor(context.getResources().getColor(R.color.color_ff8948));
			holder.iv_point_orange.setBackgroundResource(R.drawable.point_orange);
			holder.tv_notcode.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								medie.isAdd=5;  
									medie.isSlected = "1";
									holder.tv_notcode.setBackgroundResource(R.drawable.bulk_green);  
									holder.tv_reduce.setBackgroundResource(R.drawable.bulk_gray);
									holder.tv_short.setBackgroundResource(R.drawable.bulk_gray);
									
									holder.tv_notcode.setTextColor(context.getResources().getColor(R.color.color_greenline2dp));
									holder.tv_reduce.setTextColor(context.getResources().getColor(R.color.color_333333));
									holder.tv_short.setTextColor(context.getResources().getColor(R.color.color_333333));
								//list.remove(position); 
									list.set(position, medie);
								iterface.getSelect(list,list.size());
								setListViewHeightBasedOnChildren(holder.lv_list);
								notifyDataSetChanged();
							}
						});
			holder.tv_reduce.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							medie.isSlected = "2";
							holder.tv_notcode.setBackgroundResource(R.drawable.bulk_gray); 
							holder.tv_reduce.setBackgroundResource(R.drawable.bulk_green);
							holder.tv_short.setBackgroundResource(R.drawable.bulk_gray); 
							holder.tv_notcode.setTextColor(context.getResources().getColor(R.color.color_333333));
							holder.tv_reduce.setTextColor(context.getResources().getColor(R.color.color_greenline2dp));
							holder.tv_short.setTextColor(context.getResources().getColor(R.color.color_333333));
							medie.isAdd=4;
							//list.remove(position); 
							list.set(position, medie);
							iterface.getSelect(list,list.size());
							setListViewHeightBasedOnChildren(holder.lv_list);
							notifyDataSetChanged();
						}
					});
			holder.tv_short.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					medie.isSlected = "3";
					holder.tv_notcode.setBackgroundResource(R.drawable.bulk_gray); 
					holder.tv_reduce.setBackgroundResource(R.drawable.bulk_gray);
					holder.tv_short.setBackgroundResource(R.drawable.bulk_green);
					//list.remove(position); 
					
					holder.tv_notcode.setTextColor(context.getResources().getColor(R.color.color_333333));
					holder.tv_reduce.setTextColor(context.getResources().getColor(R.color.color_333333));
					holder.tv_short.setTextColor(context.getResources().getColor(R.color.color_greenline2dp));
					
					list.set(position, medie);
					iterface.getSelect(list,list.size());
					setListViewHeightBasedOnChildren(holder.lv_list);
					notifyDataSetChanged();
				}
			});
		
			
		}else if((m>require&&n>0)||(require==0)){
			holder.ll_buy_des.setVisibility(View.GONE);
			holder.ll_buy_des2.setVisibility(View.VISIBLE);
			
			holder.tv_nummore.setTextColor(context.getResources().getColor(R.color.color_0084ff));
			int showNum = 0;
			  m = 0;
			  n = 0;
			 haveBuy = 0;
			 require = 0; 
			if (0!=medie.bought_quantity) {
				haveBuy = medie.bought_quantity;
			}
			if (medie.numResone == -11111) {
				n = 0;
			}else {
				n = medie.numResone ;
			}
			if (0!=medie.requires_quantity) {
				require = medie.requires_quantity;
			}
			m = n + haveBuy;
			if (m-require>n) {
				showNum = n;
			}else {
				showNum = m-require;
			}
			holder.tv_nummore.setText("多" + showNum + medie.unit.name);
			holder.iv_point_orange.setBackgroundResource(R.drawable.circleblue);
			WrongNumAdapter adapter = new WrongNumAdapter(context, R.layout.adapter_wrongnum, medie.lists, new DeleteInterace() {

				@Override
				public void delete(List<MedieNum> objects) {
					medie.lists = (ArrayList<MedieNum>) objects;
					// TODO Auto-generated method stub
					holder.iv_point_orange.setVisibility(View.VISIBLE);
					holder.tv_nummore.setVisibility(View.VISIBLE);
					int num = 0;
					if (0 != medie.requires_quantity) {
						//num = (Integer.parseInt(medie.requires_quantity)-objects.size());
						num = CustomUtils.getData(medie.requires_quantity, medie.bought_quantity, objects.size());
						if (medie.requires_quantity < medie.bought_quantity) {
							num = objects.size();
						}
					} else if (medie.isAdd == 7) {
						num = objects.size();
					}
					if (medie.isAdd != 7) {
						if (num > 0) {
							medie.isAdd = 0;
							medie.numResone = objects.size();
							list.set(position, medie);
						} else if (num == 0) {
							medie.numResone = objects.size();
							medie.isAdd = 2;
							list.set(position, medie);
							iterface.getSelect(list, list.size());
							setListViewHeightBasedOnChildren(holder.lv_list);
							notifyDataSetChanged();
							list.remove(position);
						}
					} else {
						//当是纯粹增加的时候（药单中没有）
						if (num > 0) {
							medie.requires_quantity = 0;
							medie.isAdd = 7;
							medie.numResone = objects.size();
							list.set(position, medie);
						} else if (num == 0) {

							medie.requires_quantity = 0;
							medie.isAdd = 7;
							list.set(position, medie);
							iterface.getSelect(list, list.size());
							setListViewHeightBasedOnChildren(holder.lv_list);
							notifyDataSetChanged();
							list.remove(position);

						}

					}

					iterface.getSelect(list, list.size());
					setListViewHeightBasedOnChildren(holder.lv_list);
					notifyDataSetChanged();
				}
			});

			holder.lv_list.setAdapter(adapter);
			setListViewHeightBasedOnChildren(holder.lv_list);
			if (!medie.isOpen) {
				holder.lv_list.setVisibility(View.GONE);
				holder.iv_open_list.setBackgroundResource(R.drawable.list_closed);
			}else {
				holder.lv_list.setVisibility(View.VISIBLE);
				holder.iv_open_list.setBackgroundResource(R.drawable.list_open);
			}
			holder.iv_open_list.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					// TODO Auto-generated method stub
					if (medie.isOpen) {
						medie.isOpen = false;
						list.set(position, medie);
						holder.lv_list.setVisibility(View.GONE);
						holder.iv_open_list.setBackgroundResource(R.drawable.list_closed);
					} else {
						holder.lv_list.setVisibility(View.VISIBLE);
						holder.iv_open_list.setBackgroundResource(R.drawable.list_open);

						medie.isOpen = true;
						list.set(position, medie);
						setListViewHeightBasedOnChildren(holder.lv_list);
					}
					notifyDataSetChanged();
				}
			});
			holder.tv_plus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (medie.isAdd != 7) {
						medie.isAdd = 6;
					} else {
						medie.requires_quantity = 0;
						medie.numResone = medie.lists.size();
					}
					if (medie.isSlected.equals("7")) {
						medie.isSlected = "-1";
						holder.tv_plus.setBackgroundResource(R.drawable.btn_bulk_gray);
						holder.tv_plus.setTextColor(context.getResources().getColor(R.color.color_333333));
					} else {
						medie.isSlected = "7";
						holder.tv_plus.setBackgroundResource(R.drawable.btn_bulk_green);
						holder.tv_plus.setTextColor(context.getResources().getColor(R.color.color_greenline2dp));
					}

					//list.remove(position);
					list.set(position, medie);
					iterface.getSelect(list, list.size());
					setListViewHeightBasedOnChildren(holder.lv_list);
					notifyDataSetChanged();
				}
			});
		}
	} 
	
	private void setListViewHeightBasedOnChildren(ListView listView) {
		android.widget.ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	public static class ViewHolder extends BaseViewHolder{
	@Bind (R.id.tv_weight)
	TextView tv_weight;
	@Bind (R.id.tv_name)
	TextView tv_name;
	@Bind (R.id.tv_unit)
	TextView tv_unit;
	@Bind (R.id.tv_num)
	TextView tv_num;
	@Bind (R.id.tv_company)
	TextView tv_company;
	@Bind (R.id.tv_nummore)
	TextView tv_nummore;
	@Bind (R.id.iv_point_orange)
	ImageView iv_point_orange;
	@Bind (R.id.ll_buy_des)
	LinearLayout ll_buy_des;
	@Bind (R.id.ll_buy_des2)
	LinearLayout ll_buy_des2;
	@Bind(R.id.lv_list)
	ListView lv_list;
	@Bind(R.id.iv_open_list)
	ImageView iv_open_list;
	@Bind(R.id.tv_short)
	Button tv_short;
	@Bind(R.id.tv_reduce)
	Button tv_reduce;
	@Bind(R.id.tv_notcode)
	Button tv_notcode;
	@Bind(R.id.tv_plus)
	Button tv_plus;
	@Bind(R.id.ll_not_nomber)
	LinearLayout ll_not_nomber;
	}
	 
}
