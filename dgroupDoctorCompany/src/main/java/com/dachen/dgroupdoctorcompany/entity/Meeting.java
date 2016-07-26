package com.dachen.dgroupdoctorcompany.entity;

import java.io.Serializable;

public class Meeting implements Serializable {
    public String id;//会议记录id
    public String companyId;//公司id
    public String company;//公司名称                     
    public String subject;//标题                         
    public long startDate;//开始日期                     
    public long startTime;//开始时间                      
    public long endTime;//结束时间                       
    public int attendeesCount;//参加人数              
    public int price;//价格                
    public String organizerToken;//组织者加入口令         
    public String panelistToken;//嘉宾加入口令           
    public String attendeeToken;//普通参加者加入口令     
    public String organizerJoinUrl;//组织者加入URL        
    public String panelistJoinUrl;//嘉宾加入URL          
    public String attendeeJoinUrl;//普通参加者加入URL    
    public String liveId;//直播id                    
    public String number;//直播编号                   
    public int status;//1:未开始，2：已开始
    public String createUserName;//创建者姓名
    public String createUserId;//用户id
    public String headPicFileName;//头像
    public String domain;//域名
    public int isMyCreate;//1:是我创建，0：不是我创建的
}
