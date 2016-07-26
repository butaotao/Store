package com.dachen.dgroupdoctorcompany.entity;

/**
 * Created by Burt on 2016/3/3.
 */
public class DiseaseType extends BaseModel {

    /** serialVersionUID **/
    private static final long serialVersionUID = -8125632201347375262L;

    private String id;// AAAA001,
    private String name;// 小儿脑积水
    private String parent;
    private boolean leaf;
    private boolean isDeleteFlag;
    private boolean isDisable;
    private boolean isAddFlag;
    private int articleCount;
    private int weight;

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isAddFlag() {
        return isAddFlag;
    }

    public void setAddFlag(boolean isAddFlag) {
        this.isAddFlag = isAddFlag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeleteFlag() {
        return isDeleteFlag;
    }

    public void setDeleteFlag(boolean isDeleteFlag) {
        this.isDeleteFlag = isDeleteFlag;
    }

    public boolean isDisable() {
        return isDisable;
    }

    public void setDisable(boolean isDisable) {
        this.isDisable = isDisable;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

}