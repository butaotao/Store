package com.dachen.dgroupdoctorcompany.entity;

/**
 * Created by Burt on 2016/5/16.
 */
public class ContactsInfo {
    private String phone;
    private String name;
    private String letter;
    private String bitmap;
    private boolean isOnlyFriend;
    private boolean isGroupDoctor;
    private String toUserId;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isOnlyFriend() {
        return isOnlyFriend;
    }

    public void setOnlyFriend(boolean isOnlyFriend) {
        this.isOnlyFriend = isOnlyFriend;
    }

    public boolean isGroupDoctor() {
        return isGroupDoctor;
    }

    public void setGroupDoctor(boolean isGroupDoctor) {
        this.isGroupDoctor = isGroupDoctor;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }



}
