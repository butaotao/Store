package com.dachen.dgroupdoctorcompany.entity;

/**
 * Created by Burt on 2016/3/3.
 */
public class OfficeHead {

    private String id;
    private String title;

    /**
     * @param id
     * @param title
     */
    public OfficeHead(String id, String title) {
        super();
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "OfficeHead{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}