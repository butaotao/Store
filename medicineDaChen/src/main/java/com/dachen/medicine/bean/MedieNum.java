package com.dachen.medicine.bean;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MedieNum  implements Serializable{
	public String num = "";
	public boolean isselect;
	@Override
	public String toString() {
		return "MedieNum [num=" + num + "]";
	}
	public Object deepCopy() throws Exception {
       // 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝  
       ByteArrayOutputStream bos = new ByteArrayOutputStream();

        ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(bos);
            oos.writeObject(this);

            // 将流序列化成对象
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

            ObjectInputStream ois = new ObjectInputStream(bis);

            return ois.readObject();


}

    @Override
    public boolean equals(Object o) {
        MedieNum m = (MedieNum)o;
        if (TextUtils.isEmpty(m.num)||TextUtils.isEmpty(this.num)){
            return  false;
        }
        if (m.num.equals(this.num)){
            return true;
        }
        return false;

    }
}