package com.ub.kloudsync.activity;

import java.util.ArrayList;
import java.util.List;

public class TeamSpaceBean {

    private int itemID;
    private String name;
    private int companyID;
    private int type;
    private int parentID;
    private String createdDate;
    private String createdByName;

    private List<TeamSpaceBean>  spaceList=new ArrayList<>();

    private List<TeamUser> memberList=new ArrayList<>();

    public List<TeamUser> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<TeamUser> memberList) {
        this.memberList = memberList;
    }

    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public List<TeamSpaceBean> getSpaceList() {
        return spaceList;
    }

    public void setSpaceList(List<TeamSpaceBean> spaceList) {
        this.spaceList = spaceList;
    }
}
