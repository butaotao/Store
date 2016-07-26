package com.dachen.medicine.view;



import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dachen.medicine.R;
import com.dachen.medicine.activity.GetMedieInfo;
import com.dachen.medicine.activity.MipcaActivityCapture;
import com.dachen.medicine.activity.MipcaActivityCaptures;
import com.dachen.medicine.adapter.AdapterChoicemeDicine;
import com.dachen.medicine.bean.MedieNum;
import com.dachen.medicine.entity.CdrugRecipeitem;
import com.dachen.medicine.logic.ScaningData;
import com.dachen.medicine.logic.Sort;
import com.dachen.medicine.logic.SubString;

/**
 * Created by Burt on 2016/1/11.
 */
public class DialogChoicePatientGallery implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    Activity context;
    Dialog dialog; 
    List<CdrugRecipeitem> listdrug;
    ListView myGallery;
    Button button1; 
    AdapterChoicemeDicine adatper;
    GetMedieInfo infointerface ;
    TextView titledes;
    String id = "";
    String ids;
    public DialogChoicePatientGallery(int p,Activity con, final int refresh, final List<CdrugRecipeitem> listdrug,
                                      final String ids,final GetMedieInfo infointerface , final String scandeCode) {
        this.context = con; 
        	 dialog = new Dialog(context, R.style.addresspickerstyle);
             View mView = LayoutInflater.from(context)
                     .inflate(R.layout.dialog_choicemedicine, null);
             button1 = (Button) mView.findViewById(R.id.btn_cancel);
             titledes = (TextView) mView.findViewById(R.id.titledes);
           /*  if (ids.length()>10) {
             	 id = ids.substring(0,7)+"***"+ids.substring(ids.length()-3);
     		}else {
     			id = ids;
     		}*/
        this.ids = ids;
            id = SubString.getMediedata(ids);
             
             titledes.setText("药监码 "+id+"在系统中找不到对应的药品，请选择:");
             this.infointerface = infointerface;
             button1.setOnClickListener(this);
             dialog.setCanceledOnTouchOutside(false);
             dialog.setCancelable(false);
             myGallery = (ListView) mView.findViewById(R.id.listview);
             float mWidth = con.getResources().getDisplayMetrics().widthPixels;
             float mHeight = con.getResources().getDisplayMetrics().heightPixels;
             float mDensity = con.getResources().getDisplayMetrics().density;
             dialog.setContentView(mView);
             dialog.getWindow().setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = (int) (mWidth );
            /*    lp.height = (int) mHeight;*/
             this.listdrug = listdrug;
            /* if (!TextUtils.isEmpty(listdrug.get(listdrug.size()-1).id)&&listdrug.size()>0&&!listdrug.get(listdrug.size()-1).id.equals("01010")) {
             	 CdrugRecipeitem item = new CdrugRecipeitem();
                  item.id="01010";
                  listdrug.add(item);
     		}*/
            
             adatper  = new AdapterChoicemeDicine(context,R.layout.adapter_choicedicine,listdrug);
             myGallery.setAdapter(adatper);
             setListViewHeightBasedOnChildren();
             //myGallery.setOnItemClickListener(new OnItemClickListenerImpl());
             myGallery.setOnItemClickListener(new OnItemClickListener() {

     			@Override
     			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
     					long arg3) {
     				// TODO Auto-generated method stub
     				/*else {
     					infointerface.getdata(null);
     				}*/
                    if (refresh ==0){
                        CdrugRecipeitem bean3  =  new CdrugRecipeitem();
                        try {
                            bean3 = MipcaActivityCaptures.getDrugRecipeData((CdrugRecipeitem) listdrug.get(arg2).deepCopy(), ids);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        bean3.scaningNum = 1;
                        ArrayList list = new ArrayList();
                        MedieNum num = new MedieNum();
                        num.num =ids;
                        list.add(num);
                       // bean3.lists = list;

                        MipcaActivityCaptures.listScaning.add(bean3);
                        MipcaActivityCaptures.listScaning_false.add(bean3);
                        ScaningData.getlistScaning_showIntitleBar().add(bean3);
                        Sort.sortMedie(ScaningData.getlistScaning_showIntitleBar());
                        setListViewHeightBasedOnChildren();

                    }
                    if (arg2<=(listdrug.size()-1)) {
                        infointerface.getdata(listdrug.get(arg2));
                    }
                    dialog.dismiss();
                    dialog = null;
     			}
     		}); 
       
    }
    //app在前台启动
    public static boolean isRunningForeground (Context context){
        String packageName = context.getApplicationContext().getPackageName();
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if(!TextUtils.isEmpty(cn.getClassName()) && cn.getClassName().contains("ipca"))
        {
            return true ;
        }

        return false ;
    }
    private void setListViewHeightBasedOnChildren() {

        android.widget.ListAdapter listAdapter = myGallery.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        if (null!=listAdapter&&listdrug.size()>0) {
            if (listdrug.size()>4) {
                for (int i = 0; i < 4; i++) {
                    View listItem = listAdapter.getView(0, null, myGallery);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                    //ToastUtils.showToast("num=="+num);
                }
            }else  {
                for (int i = 0; i < listdrug.size(); i++) {
                    View listItem = listAdapter.getView(0, null, myGallery);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
            //listView.setSelection(listView.getBottom());
         //   myGallery.setSelection(ListView.FOCUS_DOWN);
        }

        ViewGroup.LayoutParams params = myGallery.getLayoutParams();
        params.height = totalHeight
                + (myGallery.getDividerHeight() * (listAdapter.getCount() - 1));
        myGallery.setLayoutParams(params);
    }
    public void show() {
        if (null!=context&&isRunningForeground(context)){
            dialog.show();
        }

    }

    public void hide() {
        dialog.hide();
    }

    public void dismiss() {
        dialog.dismiss();
    }
    public boolean isShow(){
    	return dialog.isShowing();
    }
    @Override
    public void onClick(View v) {
    if (v.getId()==R.id.btn_cancel){
    	infointerface.getdata(null);
        CdrugRecipeitem bean3  =  new CdrugRecipeitem();
        bean3.scanCode = ids;
        bean3.isAdd = 0;
        bean3.general_name = "";
        bean3.trade_name = "";
        bean3.id = MipcaActivityCaptures.SCORD_NOTFOUND_ONSERVER;
        bean3.manufacturer = MipcaActivityCaptures.SCORD_NOTFOUND_ONSERVER;
        bean3.createtime = System.currentTimeMillis();
        MipcaActivityCaptures.listScaning.add(bean3);

        MipcaActivityCaptures.listScaning_false.add(bean3);
            dialog.dismiss();;
        } 
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    	  dialog.dismiss();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
