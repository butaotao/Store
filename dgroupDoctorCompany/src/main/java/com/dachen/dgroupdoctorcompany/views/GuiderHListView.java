package com.dachen.dgroupdoctorcompany.views;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;

import com.dachen.dgroupdoctorcompany.adapter.CompanyListGuide;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/8/23下午7:40.
 * @描述 水平导航列表
 */
public class GuiderHListView extends HListview2 {
    Context mContext;
    private HListview2 mHListView2;
    private ArrayList<Guider> mListGuide;              //导航Listview数据
    private Map<Integer, ArrayList<Guider>> departList;   //导航Listview数据任务栈
    private CompanyListGuide mListGuideAdapter;
    private Map<Integer, Map<String , ArrayList<String>>> listGuideMap; //公司部门id任务栈;
    private int currentPosition = 0;//任务栈任务数

    int oldPosition;

    public GuiderHListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @Override
    public LayoutTransition getLayoutTransition() {
        return super.getLayoutTransition();
    }

    private void initView(Context context) {
        mContext = context;
        mHListView2 = this;
        mListGuide = new ArrayList<>();//储存部门列表
        departList = new LinkedHashMap<>();
        listGuideMap = new LinkedHashMap<>();
    }

    /**
     * 设置外部Adapter
     */
    public void setAdapter(CompanyListGuide adapter) {
        if (adapter != null) {
            mListGuideAdapter = adapter;
            mHListView2.setAdapter(mListGuideAdapter);
        }
    }

    /**
     * 设置内置Adapter
     */
    public void setAdapter() {
        mListGuideAdapter = new CompanyListGuide(mContext, mListGuide);
        mHListView2.setAdapter(mListGuideAdapter);
    }

    /**
     * 添加新的任务到任务栈
     *
     * @param departName 名字
     * @param departId   数据id
     */
    public void addTask(String departName, String departId) {
        if (mListGuide != null && listGuideMap != null && departList != null) {
            Guider guider = new Guider(departId,departName);
            mListGuide.add(guider);
            departList.put(currentPosition,copyToNewList(mListGuide));
            currentPosition++;
        }
    }

    /**
     * 移除最后任务
     *
     * @return
     */
    public void reMoveTask() {
        if (mListGuide != null && listGuideMap != null && departList != null) {
            int position = currentPosition - 2;//当前任务栈id数
            ArrayList<Guider> guiders = departList.get(position);
            mListGuide.clear();
            mListGuide.addAll(guiders);
            departList.remove(position+1);
            --currentPosition;
        }
    }

    /**
     * 得到倒数第二个的任务id
     *
     * @return id
     */
    public String reMoveTaskId() {
        if (departList !=null) {
            int position = currentPosition - 2;//当前任务栈id数
            ArrayList<Guider> guiders = departList.get(position);
            Guider guider = guiders.get(guiders.size() - 1);
            return guider.id;
        }
        return "";
    }

    /**
     * 跳到指定任务栈
     *
     * @param position
     */
    public void addBackTask(int position) {
        if (mListGuide != null && listGuideMap != null && departList != null) {
            int forCount = oldPosition - position;
            for (int i = 0; i < forCount; i++) {
                mListGuide.remove(oldPosition - i);
            }
            departList.put(currentPosition,copyToNewList(mListGuide));
            currentPosition++;
            oldPosition = mListGuideAdapter.getCount() - 1;
        }
    }

   public String  getBackTaskId(int position){
       if (mListGuideAdapter!=null) {
           Guider item =  mListGuideAdapter.getItem(position);
           return item.id;
       }
       return "";
   }

    public void setOldPosition() {
        this.oldPosition = mListGuideAdapter.getCount() - 1;
    }

    public void notifyDataSetChanged() {
        mListGuideAdapter.notifyDataSetChanged();
    }

    /**
     * 将集合拷贝到一个新集合中
     */
    public ArrayList<Guider> copyToNewList(ArrayList<Guider> list) {
        ArrayList<Guider> arrayList = new ArrayList<>();
        arrayList.addAll(list);
        return arrayList;
    }

    /**
     * 清空说有数据
     */
    public void clearData() {
        listGuideMap.clear();
        listGuideMap = null;
        mListGuide.clear();
        mListGuide = null;
        departList.clear();
        departList = null;
    }

    public String getLastDerpartName(int position) {
        if (departList !=null) {
            ArrayList<Guider> guiders = departList.get(position);
            return guiders.get(guiders.size()-1).name;
        }
        return "";
    }

    public ArrayList<Guider> getListGuide() {
        return mListGuide;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

   public class Guider {
       public Guider(String id, String name) {
           this.id = id;
           this.name = name;
       }

       public Guider() {}

       public String id ;
        public String name;
    }
}
