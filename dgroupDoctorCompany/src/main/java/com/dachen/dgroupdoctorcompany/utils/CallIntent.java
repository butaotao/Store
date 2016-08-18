package com.dachen.dgroupdoctorcompany.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.dachen.dgroupdoctorcompany.activity.ChoiceDoctorForChatActivity;
import com.dachen.dgroupdoctorcompany.activity.ChoiceDoctorForVisitActivity;
import com.dachen.dgroupdoctorcompany.activity.MainActivity;
import com.dachen.dgroupdoctorcompany.activity.SelectPeopleActivity;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.dachen.dgroupdoctorcompany.fragment.AddressList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Burt on 2016/3/3.
 */
public class CallIntent {
    public static List<CompanyContactListEntity> listss;
    public static SelectPeopleDataInterface getSelectData;
    public interface SelectPeopleDataInterface{
        void getData(List<BaseSearch> listentitys);
    }

    /**
     *
     * @param context
     * @param groupUsers
     * @param groupType 会话组类型 如果是新建的话就传 -1
     */
    public static void SelectPeopleActivity(final Context context,ArrayList<CompanyContactListEntity> groupUsers,int groupType){
        Intent intent;
        intent = new Intent(context, SelectPeopleActivity.class);
        intent.putExtra(SelectPeopleActivity.INTENT_EXTRA_GROUP_USERS,groupUsers);
        intent.putExtra(SelectPeopleActivity.INTENT_EXTRA_GROUP_TYPE,groupType);
        // CompanyContactListEntity
//        listss = new ArrayList<>();
//        getSelectData = new SelectPeopleDataInterface(){
//            @Override
//            public void getData(List<BaseSearch> listentitys) {
//                listss.clear();
//                for (int i=0;i<listss.size();i++){
//                    CompanyContactListEntity entities = (CompanyContactListEntity)listss.get(i);
//                    listss.add(listss.get(i));
//                }
//                ToastUtils.showToast(context, "lists.size==" + listss.size());
//            }
//        };
        context.startActivity(intent);
    }
    public static void SelectPeopleActivityForResult(final Activity context,String groupId,ArrayList<CompanyContactListEntity> groupUsers,int groupType,int requestCode){
        Intent intent;
        intent = new Intent(context, SelectPeopleActivity.class);
        intent.putExtra(SelectPeopleActivity.INTENT_EXTRA_GROUP_USERS,groupUsers);
        intent.putExtra(SelectPeopleActivity.INTENT_EXTRA_GROUP_ID,groupId);
        intent.putExtra(SelectPeopleActivity.INTENT_EXTRA_GROUP_TYPE,groupType);
        context.startActivityForResult(intent, requestCode);
    }
    public static void selectDoctor(final Activity context){
        Intent intent;
        intent = new Intent(context, ChoiceDoctorForChatActivity.class);
        context.startActivity(intent);
    }
    public static void selectDoctorForVisit(final Context context){
        Intent intent;
        intent = new Intent(context, ChoiceDoctorForVisitActivity.class);
        context.startActivity (intent);
    }
    public static void startMainActivity(final Activity context){
        context.finish();
        Intent intent;

        intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        intent = new Intent(MainActivity.action);
        intent.putExtra("tab", 0);
        context.sendBroadcast(intent);
    }
}