package com.dachen.dgroupdoctorcompany.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.dachen.medicine.entity.Result;

import java.util.ArrayList;

/**
 * Created by weiwei on 2016/5/23.
 */
public class OrgEntity extends Result {
    public String detailMsg;
    public ArrayList<Data>   data;
    public static  class Data implements Parcelable {
        public Data(long creatorDate,String desc,String enterpriseId,String id,String name,
                    String parentId,ArrayList<Data> subList,int updator,long updatorDate,int creator,boolean isChexk){
            this.creatorDate = creatorDate;
            this.desc = desc;
            this.enterpriseId = enterpriseId;
            this.id = id;
            this.name = name;
            this.parentId = parentId;
            this.subList = subList;
            this.updator = updator;
            this.updatorDate = updatorDate;
            this.creator = creator;
            this.isCheck = isChexk;
        }
        public long creatorDate;
        public String desc;
        public String enterpriseId;
        public String id;
        public String name;
        public String parentId;
        public ArrayList<Data> subList;
        public int updator;
        public long updatorDate;
        public int creator;
        public boolean isCheck;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(creatorDate);
            dest.writeString(desc);
            dest.writeString(enterpriseId);
            dest.writeString(id);
            dest.writeString(name);
            dest.writeString(parentId);
            dest.writeList(subList);
            dest.writeInt(updator);
            dest.writeLong(updatorDate);
            dest.writeInt(creator);
            dest.writeString(String.valueOf(isCheck));
        }

        public static final Parcelable.Creator<Data>CREATOR = new Parcelable.Creator<Data>(){

            @Override
            public Data createFromParcel(Parcel source) {
                return new Data(source);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };
        private Data(Parcel source){
            creatorDate = source.readLong();
            desc = source.readString();
            enterpriseId = source.readString();
            id = source.readString();
            name = source.readString();
            parentId = source.readString();
            subList = new ArrayList<>();
            source.readList(subList,Data.class.getClassLoader());
            updator = source.readInt();
            updatorDate = source.readLong();
            creator = source.readInt();
            isCheck = Boolean.valueOf(source.readString());
        }
    }
}
