package com.dachen.dgroupdoctorcompany.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.dachen.dgroupdoctorcompany.adapter.CompanyListGuide;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/8/23下午7:40.
 * @描述 TODO
 */
public class GuiderHListView extends HorizontalListView {
    Context mContext;
    private HorizontalListView mHListView;
    private ArrayList<String> mListGuide ;              //导航Listview数据
    private Map<Integer,ArrayList<String>> departList ;   //导航Listview数据任务栈
    private CompanyListGuide mListGuideAdapter;
    private Map<Integer, String > listGuideMap ; //公司部门id任务栈;
    private int currentPosition = 0;//任务栈任务数


    public GuiderHListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mHListView = this;
        mListGuide = new ArrayList<>();
        departList = new LinkedHashMap<>();
        listGuideMap = new LinkedHashMap<>();
    }


    public void setAdapter(CompanyListGuide adapter){
        mListGuideAdapter = adapter;
        mHListView.setAdapter(mListGuideAdapter);
    }
    public void setAdapter(){
        mListGuideAdapter = new CompanyListGuide(mContext,mListGuide);
        mHListView.setAdapter(mListGuideAdapter);
    }



    public void addTask(String departName,String departId){
        mListGuide.add(departName);
        departList.put(currentPosition, copyToNewList(mListGuide));
        listGuideMap.put(currentPosition++, departId);
    }

    public String reMoveTask(){
        int position = currentPosition-2;//当前任务栈id数
       String  idDep = listGuideMap.get(position);//得到任务栈的前一个任务id
        mListGuide.clear();
        mListGuide.addAll(departList.get(position));    //得到前一个任务栈导航列表数据集合
        listGuideMap.remove(position+1);//移除当前任务
        departList.remove(position+1);//移除
        --currentPosition;
        notifyDataSetChanged();
        return idDep;
    }
    int oldPosition;
    public void addBackTask(int position){

        int forCount = oldPosition - position;
        Log.d("zxy", "onItemClick: oldPosition = "+oldPosition+", position = "+position);
        for (int i = 0; i < forCount; i++) {
            Log.d("zxy", "onItemClick: remove");
                mListGuide.remove(oldPosition -i);
        }
        departList.put(currentPosition,copyToNewList(mListGuide));
        listGuideMap.put(currentPosition++, listGuideMap.get(position));
        String idDep = listGuideMap.get(position);
        Log.d("zxy", "onItemClick: currentPosition = "+currentPosition+", idDep = "+listGuideMap.get(position)+", mListGuide"+mListGuide);
        oldPosition = position;
    }

    public void setOldPosition(int oldPosition){
        this.oldPosition = oldPosition;
    }

    public void notifyDataSetChanged(){
        mListGuideAdapter.notifyDataSetChanged();
    }



    /**
     * 将集合拷贝到一个新集合中
     */
    public ArrayList<String> copyToNewList(ArrayList<String> list){
        ArrayList<String > arrayList = new ArrayList<>();
        arrayList.addAll(list);
        return arrayList;
    }

    public void clearData(){
        listGuideMap.clear();
        listGuideMap = null;
        mListGuide.clear();
        mListGuide = null;
        departList.clear();
        departList = null;
    }


    public ArrayList<String> getListGuide() {
        return mListGuide;
    }

    public void setListGuide(ArrayList<String> listGuide) {
        mListGuide = listGuide;
    }

    public CompanyListGuide getListGuideAdapter() {
        return mListGuideAdapter;
    }
}