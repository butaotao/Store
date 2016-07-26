package com.dachen.dgroupdoctorcompany.adapter;

import android.content.Context;

import com.dachen.dgroupdoctorcompany.entity.ContactList;

import java.util.List;

/**
 * Created by Burt on 2016/2/20.
 */
public class AddressAdapter extends BaseCustomAdapter<ContactList>{

    public AddressAdapter(Context context, int resId, List<ContactList> objects) {
        super(context, resId, objects);
    }

    @Override
    protected EidtColleagueAdapter.ViewHolder getViewHolder() {
        return null;
    }

    @Override
    protected void fillValues(BaseViewHolder baseViewHolder, int position) {

    }

}
