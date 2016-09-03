package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.activity.SearchContactActivity;
import com.dachen.dgroupdoctorcompany.db.dbentity.Doctor;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.utils.HtmlTextViewEdit;
import com.dachen.medicine.common.utils.SharedPreferenceUtil;
import com.dachen.medicine.common.utils.StringUtils;
import com.dachen.medicine.net.CustomImagerLoader;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Burt on 2016/2/26.
 */
public class SearchContactAdapter extends BaseCustomAdapter<BaseSearch>{
    int partSize;
    int hospitalSize;
    List<CompanyContactListEntity> contact;
    List<Doctor> doctors;
    public List<BaseSearch> objects;
    SearchContactActivity.RefreshDataInterface refreshDataInterface;
    boolean isShowMore = true;
    String seachdoctor;
    boolean showSelect;
    SearchContactActivity activity;
    Context context;
    public SearchContactAdapter(Context context, int resId, List<BaseSearch> objects,List<CompanyContactListEntity> contact,
                                List<Doctor> doctors ,SearchContactActivity.RefreshDataInterface refreshDataInterface,String seachdoctor) {
        super(context, resId, objects);
        this.context = context;
        this.contact = contact;
        this.doctors = doctors;
        this.objects =objects;
        this.seachdoctor = seachdoctor;
        this.refreshDataInterface = refreshDataInterface;
        if(context instanceof SearchContactActivity){
            activity = (SearchContactActivity) context;
        }
    }
    public void setisShowMore(boolean isShowMore){
        this.isShowMore=isShowMore;
    }
    public void setContact( List<CompanyContactListEntity> contact){
        this.contact = contact;
    }
    public void setDoctors(List<Doctor> doctors){
        this.doctors = doctors;
    }
    public void setShowSelect(boolean showSelect){
        this.showSelect = showSelect;
    }
    @Override
    protected BaseViewHolder getViewHolder() {
        return new ViewHolder();
    }
//SearchContactListEntity.SearchContactEntity
    @Override
    protected void fillValues(BaseViewHolder baseViewHolder, int position) {
        BaseSearch base;
        base = (BaseSearch)getItem(position);
        final ViewHolder holder = (ViewHolder)baseViewHolder;
        holder.iv_irr.setVisibility(View.VISIBLE);
        if(base instanceof CompanyContactListEntity){
            CompanyContactListEntity  people = (CompanyContactListEntity)base;
            holder.tv_name_phone.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(people.position)/*&&!activity.showColleague*/){
                holder.tv_nameright.setText(people.position);
                holder.tv_nameright.setVisibility(View.GONE);
            }else{
                holder.tv_nameright.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(people.name)){
                boolean show = true;
                Spanned spanned = HtmlTextViewEdit.showkeywordContent(people.name,activity.searchText,context);
                if (activity!=null/*&&activity.showColleague*/){
                    if (people.name.toLowerCase().contains(activity.searchText.toLowerCase())){
                        holder.tv_name_leader.setText(spanned);
                        show = false;
                    }else {
                        holder.tv_name_leader.setText(people.name);
                    }
                    holder.tv_name_phone.setVisibility(View.GONE);
                    if (show&&!"1".equals(activity.searchText)&&people.telephone.contains(activity.searchText)){
                        Spanned spannedphone = HtmlTextViewEdit.showkeywordContent("("+people.telephone+")",activity.searchText,context);
                        holder.tv_name_phone.setText(spannedphone);
                        holder.tv_name_phone.setVisibility(View.VISIBLE);
                    }
                    boolean isEnglish = StringUtils.isEnglish(activity.searchText);
                    if (isEnglish&&show){
                        Spanned spannedphone = HtmlTextViewEdit.showkeywordContent("("+activity.searchText+")",activity.searchText,context,people);
                        holder.tv_name_phone.setText(spannedphone);
                        holder.tv_name_phone.setVisibility(View.VISIBLE);
                    }

                    holder.iv_irr.setVisibility(View.GONE);
                }else {
                    holder.tv_name_leader.setText(people.name);
                }
            }else{
                holder.tv_name_leader.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(people.department)){
                holder.tv_leader_position.setVisibility(View.VISIBLE);
                holder.tv_leader_position.setText(people.department);
            }else{
                holder.tv_leader_position.setVisibility(View.GONE);
            }


            /*if (!TextViewUtils.isEmpty(people.name)){
                holder.tv_leader_position_right.setText(people.name);
            }
            //holder.tv_leader_position.setText(people.department);*/
                if (position==0){
                    holder.tv_hosorcom.setText("企业好友");
                    holder.rl_above.setVisibility(View.VISIBLE);
                }else {
                    holder.rl_above.setVisibility(View.GONE);
                }
            if (position == 2&&partSize>=3&&isShowMore){
                holder.rl_below.setVisibility(View.VISIBLE);
                holder.rl_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.rl_below.setVisibility(View.GONE);
                        objects.clear();
                        objects.addAll(contact);
                         isShowMore = false;
                        refreshDataInterface.refreshData();
                        activity.showColleague = true;
                        notifyDataSetChanged();

                    }
                });
            }else {
                holder.rl_below.setVisibility(View.GONE);
            }
            if (activity.showColleague){
                holder.rl_below.setVisibility(View.GONE);
            }
                holder.tv_leader_position_right.setVisibility(View.GONE);
//            ImageLoader.getInstance().displayImage(people.headPicFileName, holder.head_icon);
            CustomImagerLoader.getInstance().loadImage(holder.head_icon, people.headPicFileName);
            holder.btn_radio.setBackgroundResource(R.drawable.icon_pay_unselect);
            if (people.select){
                //   holder.btn_radio.setSelected(true);
                holder.btn_radio.setBackgroundResource(R.drawable.icon_pay_selected);
            }else {
                //  holder.btn_radio.setSelected(false);

            }

            if (people.haveSelect==true||people.userId.equals(SharedPreferenceUtil.getString(mContext, "id", ""))){
                holder.btn_radio.setBackgroundResource(R.drawable.icon_pay_disable);
            }

        }else if(base instanceof Doctor){
            Doctor doctor = (Doctor) base;
            if (activity!=null/*&&activity.showDoctor*/&&!TextUtils.isEmpty(activity.searchText)){
                Spanned spanned = HtmlTextViewEdit.showkeywordContent(doctor.name,activity.searchText,context);
                holder.tv_name_leader.setText(spanned);
                holder.iv_irr.setVisibility(View.GONE);
                holder.tv_nameright.setVisibility(View.GONE);
            }else {
                if (TextUtils.isEmpty(doctor.name)){
                    holder.tv_name_leader.setText(doctor.name);
                }else {
                    holder.tv_name_leader.setText("");
                }
                holder.tv_nameright.setVisibility(View.GONE);
           }
            holder.tv_leader_position.setText(doctor.hospital);
            holder.tv_nameright.setText(doctor.title);
            holder.tv_leader_position_right.setText(doctor.departments);
            if (TextUtils.isEmpty(seachdoctor)){
                if (position==0||((position==partSize)&&hospitalSize!=0)){
                    holder.tv_hosorcom.setText("医生通讯录");
                    holder.rl_above.setVisibility(View.VISIBLE);
                }else {
                    holder.rl_above.setVisibility(View.GONE);
                }
                if (position == getCount()-1&&hospitalSize>=3&&isShowMore){
                    holder.rl_below.setVisibility(View.VISIBLE);
                    holder.rl_search.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            objects.clear();
                            objects.addAll(doctors);
                            isShowMore = false;
                            refreshDataInterface.refreshData();
                            notifyDataSetChanged();
                        }
                    });
                }else {
                    holder.rl_below.setVisibility(View.GONE);
                }
            }else {
                holder.rl_above.setVisibility(View.GONE);
                holder.rl_below.setVisibility(View.GONE);
            }
            //ImageLoader.getInstance().displayImage(doctor.headPicFileName,holder.head_icon);
            CustomImagerLoader.getInstance().loadImage(holder.head_icon, doctor.headPicFileName,
                    R.drawable.head_icons_company, R.drawable.head_icons_company);
        }
        holder.rl_above.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.tv_nameright.setVisibility(View.GONE);
     /*   if (activity.isShow){
            holder.btn_radio.setVisibility(View.VISIBLE);
        }else {*/
            holder.btn_radio.setVisibility(View.GONE);
       // }
    }
    public void setPartSize(int partSize){
        this.partSize = partSize;
    }
    public void sethospitalSize(int hospitalSize){
        this.hospitalSize = hospitalSize;
    }
    public class ViewHolder extends BaseViewHolder{
        //企业通讯录 医生通讯录 title 描述
        @Bind(R.id.tv_des)
        TextView tv_des;
        @Bind(R.id.head_icon)
        ImageView head_icon;
        @Bind(R.id.tv_name_leader)
        TextView tv_name_leader;
        @Bind(R.id.tv_nameright)
        TextView tv_nameright;
        @Bind(R.id.tv_leader_position)
        TextView tv_leader_position;
        @Bind(R.id.tv_leader_position_right)
        TextView tv_leader_position_right;
        @Bind(R.id.rl_above)
        RelativeLayout rl_above;
        @Bind(R.id.rl_search)
        RelativeLayout rl_search;
        @Bind(R.id.tv_hosorcom)
        TextView tv_hosorcom;
        @Bind(R.id.rl_below)
        RelativeLayout rl_below;
        @Bind(R.id.btn_radio)
        RadioButton btn_radio;
        @Bind(R.id.iv_irr)
        ImageView iv_irr;
        @Bind(R.id.tv_name_phone)
        TextView tv_name_phone;
    }
}
