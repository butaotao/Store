package com.dachen.mediecinelibraryrealizedoctor.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.medicine.common.utils.ImageUtil;
import com.dachen.medicine.common.utils.MedicineApplication;
import com.dachen.medicine.common.utils.ToastUtils;
import com.dachen.medicine.config.UserInfo;
import com.dachen.medicine.entity.Result;
import com.dachen.medicine.net.CustomImagerLoader;
import com.dachen.medicine.net.HttpManager;
import com.dachen.medicine.net.HttpManager.OnHttpListener;
import com.dachen.medicine.net.Params;
import com.dachen.medicine.view.AddAndSubView;
import com.dachen.medicine.view.AddAndSubView.OnNumChangeListener;
import com.dachen.mediecinelibraryrealizedoctor.R;
import com.dachen.mediecinelibraryrealizedoctor.activity.BaseActivity;
import com.dachen.mediecinelibraryrealizedoctor.activity.GetMedieDetaiInfo;
import com.dachen.mediecinelibraryrealizedoctor.entity.AddDoctorCollectionInfo;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicienInfo;
import com.dachen.mediecinelibraryrealizedoctor.entity.MedicineInfo;
import com.dachen.mediecinelibraryrealizedoctor.utils.CompareDatalogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PreparedMedieAdapters extends BaseAdapter {
    AddAndSubView addAndSubView1;
    BaseActivity context;
    public List<MedicineInfo> list;
    ChoiceItemNumInterface numChoices;
    int number;
    protected LayoutInflater mInflater;
    ViewHolder holder;
    public int isclick = 0;
    HashMap<String, String> maps;
    int clickPosition;

    public PreparedMedieAdapters(BaseActivity context,
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

    public PreparedMedieAdapters(BaseActivity context,
                                 List<MedicineInfo> objects,
                                 ChoiceItemNumInterface numChoice, int isclick) {
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
            holder.rl_medicinename = (RelativeLayout) v.findViewById(R.id.rl_medicinename);
            holder.ll_numshow_text = (LinearLayout) v.findViewById(R.id.ll_numshow_text);
            holder.tv_packages = (TextView) v.findViewById(R.id.tv_packages);
            holder.iv_company = (ImageView) v.findViewById(R.id.iv_company);
            holder.iv_image = (ImageView) v.findViewById(R.id.iv_pared);
            holder.img_company = (ImageView) v.findViewById(R.id.img_company);
            holder.rl_pared = (RelativeLayout) v.findViewById(R.id.rl_pared);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.ll_diviver.setVisibility(View.GONE);
        holder.ll_childern.setVisibility(View.VISIBLE);
        final MedicineInfo media = (MedicineInfo) getItem(position);
        holder.tv_packages.setText(media.goods$pack_specification);
        String name = media.goods$general_name;

        String goodsname = "";
            goodsname = media.title;

        if (TextUtils.isEmpty(name)) {
            name = media.trade_name;
        }
        String names = CompareDatalogic.getShowName2(media.goods$general_name, name, goodsname,
                holder.tv_medicine_name, media.goods$specification, context);
        //StringUtils.get(holder.tv_medicine_name, name, media.goods$specification, context);
        holder.tv_medicine_name.setText(names);

        holder.tv_medicine_height.setText(media.goods$specification);
        holder.tv_company.setText(media.goods$manufacturer);
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
            holder.tv_company.setText("未定厂商");
        }


        //LogUtils.burtLog();
        String url = media.goods$image;
        if (TextUtils.isEmpty(url)) {
            url = media.goods$image_url;
        }
        if (!TextUtils.isEmpty(url)) {
            String ip = maps.get("ip");
            String urls = ip + ImageUtil.getUrl("") + url + "?a=" + maps.get("session");
            urls = ImageUtil.getUrl(url, ip, maps.get("session"), 1);
            CustomImagerLoader.getInstance().loadImage(holder.img_listview,
                    urls);
        } else {
            holder.img_listview
                    .setImageResource(R.drawable.image_download_fail_icon);
        }

        final GetMedieDetaiInfo info = new GetMedieDetaiInfo(context, media);
        if (null != media.goods$manufacturer && UserInfo.getInstance(context).getUserType().equals("3")) {
            holder.rl_medicinename.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    info.ShowDialog();
                }
            });
            final String finalUrl = url;
            holder.img_listview.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    info.ShowDialog();
                }
            });
            holder.ll_numshow_text.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    info.ShowDialog();
                }
            });
        }


        holder.subview_add.setButtonLayoutParm(34, 34);
        holder.subview_add.setViewListener();
        holder.subview_add.setButtonBgResource(R.drawable.imports, R.drawable.addpress);
        if (null != holder.ll_numshow) {
            if (media.getNum() > 0) {
                holder.subview_add.setSubVisiAble();
                holder.iv_single.setVisibility(View.INVISIBLE);
                holder.subview_add.setVisibility(View.VISIBLE);

                holder.subview_add.setNum(media.getNum());

                holder.subview_add.setOnNumChangeListener(new OnNumChangeListener() {
                    @Override
                    public void onNumChange(View view, int num) {
                        //LogUtils.burtLog("num=="+num);
                        MedicineInfo entity = (MedicineInfo) getItem(position);
                        if (null != entity) {

                            if (entity.num >= 0) {
                                entity.num = (num);
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
                        if (num > 0) {
                            holder.iv_single.setVisibility(View.INVISIBLE);
                            holder.subview_add.setVisibility(View.VISIBLE);
                            holder.subview_add.setSubVisiAble();
                        } else {
                            holder.iv_single.setVisibility(View.VISIBLE);
                            //holder.subview_add.setVisibility(View.INVISIBLE);
                            holder.subview_add.setSubInvisiAble();
                        }

                        notifyDataSetChanged();
                    }
                });

            } else {
                holder.subview_add.setSubInvisiAble();
                holder.subview_add.setOnNumChangeListener(new OnNumChangeListener() {
                    @Override
                    public void onNumChange(View view, int num) {
                        MedicineInfo entity = (MedicineInfo) getItem(position);
                        if (null != entity) {

                            if (entity.num >= 0) {
                                entity.num = (1);
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
        holder.iv_company.setVisibility(View.GONE);
        if (null == media.goods$manufacturer || isclick == 1) {
            holder.subview_add.setVisibility(View.GONE);
            holder.iv_company.setVisibility(View.VISIBLE);
        } else {
            holder.subview_add.setVisibility(View.VISIBLE);
        }

        if (isclick == 2) {
            holder.iv_image.setVisibility(View.GONE);

        }
        String type = UserInfo.getInstance(context).getUserType();
        if (isclick == 3 || !type.equals("3")) {
            holder.iv_image.setVisibility(View.GONE);
            holder.img_company.setVisibility(View.GONE);
        }
        if (/*!TextUtils.isEmpty(ChoiceMedicineActivity.groupID)*/UserInfo.getInstance(context).getUserType().equals("3")) {
            holder.iv_image.setVisibility(View.VISIBLE);
            holder.img_company.setVisibility(View.GONE);
            //	if(media.is_group_goods){
            //holder.iv_image.setBackgroundResource(R.drawable.no_pared);
            if (media.is_group_goods) {
                holder.img_company.setVisibility(View.VISIBLE);
            } else {
                holder.img_company.setVisibility(View.GONE);
            }

            if (media.is_doctor_cb) {
                holder.iv_image.setBackgroundResource(R.drawable.pared);
            } else {
                holder.iv_image.setBackgroundResource(R.drawable.no_pared);
            }
            holder.rl_pared.setOnClickListener(new OnClickListener() {
                @SuppressWarnings("unchecked")
                @Override
                public void onClick(View arg0) {
                        /*	if (TextUtils.isEmpty(ChoiceMedicineActivity.groupID)) {
								ToastUtils.showToast("医生ID为空！");
								return;
							} */
                    // TODO Auto-generated method stub
                    //ToastUtils.showToast("media=="+media.goods$general_name);
                    if (null != media.goods) {
                        HashMap<String, String> interfaces = new HashMap<String, String>();
                        interfaces.clear();
                        String config  ="";
                        interfaces.put("access_token", UserInfo.getInstance(context).getSesstion());

                        String id="";

                        if (UserInfo.getInstance(context).getUserType().equals("3")){
                            id = media.goods.id;
                        }
                        context.showLoadingDialog();
                        if (!media.is_doctor_cb) {
                            config  ="org/drugCollection/addDrugCollection";
                            interfaces.put("goodsId", id);
                            interfaces.put("userType",UserInfo.getInstance(context).getUserType());
                            interfaces.put("userId", UserInfo.getInstance(context).getId());
                           // interfaces.put("goodsGroupId",media.id);
                        }else {
                            config ="org/drugCollection/deleteByDoctorIdAndGoodsId";
                            interfaces.put("doctorId", UserInfo.getInstance(context).getId());
                            interfaces.put("goodsId",id);
                        }

                        //ToastUtils.showToast("media.id_doctor_cb=="+media.id_doctor_cb);
                        //postData(media.goods.id,ChoiceMedicineActivity.groupID);
                        new HttpManager().post(context, config, AddDoctorCollectionInfo.class,
                                interfaces, new OnHttpListener() {
                                    @Override
                                    public void onFailure(
                                            Exception arg0,
                                            String arg1, int arg2) {
                                        context.closeLoadingDialog();
                                        // ToastUtils.showToast(context,context.getString(R.string.data_exception));
                                        // TODO Auto-generated method stub
                                        //holder.iv_image.setBackgroundResource(R.drawable.no_pared);
                                    }
                                    @Override
                                    public void onSuccess(Result arg0) {
                                        context.closeLoadingDialog();
                                        AddDoctorCollectionInfo info = (AddDoctorCollectionInfo) arg0;
                                        // TODO Auto-generated method stub
                                        if (info.resultCode ==1 && info.data!=null&&
                                                !TextUtils.isEmpty(info.data.drugCollectionId)
                                                ) {
                                            holder.iv_image.setBackgroundResource(R.drawable.pared);
                                            ToastUtils.showToast(context, context.getString(R.string.havecollection));
                                            holder.iv_image.setBackgroundResource(R.drawable.pared);
                                            media.is_doctor_cb = true;
                                          /*  String s = arg0.resultMsg;
                                            media.id_doctor_cb = s.replace("\"", "");*/
                                            list.set(position, media);
                                            notifyDataSetChanged();
                                        } else if (info.resultCode ==1  &&  info.data==null) {
                                            ToastUtils.showToast(context, context.getString(R.string.cancelcollection));
                                            media.is_doctor_cb = false;
                                            int lengthMedie = 0;

                                            holder.iv_image.setBackgroundResource(R.drawable.no_pared);
                                            list.set(position, media);
                                            notifyDataSetChanged();
                                        } else {
                                            ToastUtils.showToast(context, "修改失败！");
                                        }
                                    }
                                    @Override
                                    public void onSuccess(
                                            ArrayList arg0) {
                                        // TODO Auto-generated method stub
                                        context.closeLoadingDialog();
                                    }
                                }, false, 1);
                    }

                }
            });
				/*}else {
					holder.iv_image.setBackgroundResource(R.drawable.pared);
					holder.iv_image.setVisibility(View.GONE);
					holder.img_company.setVisibility(View.GONE);
				}*/

        }


        return v;

    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
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


    public static class ViewHolder {
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
        ImageView iv_image;
        ImageView img_company;
        RelativeLayout rl_pared;
    }

    public void setNotfiDataSetChanged2(List<MedicineInfo> list) {
        // TODO Auto-generated method stub
        this.list = list;
        notifyDataSetChanged();
    }
}
