package com.example.sunlianglong.messageServer;

/**
 * Created by sun liang long on 2017/11/16.
 */

public class Friends {
    private String ip;
    private String netname;
    private String geqian;
    private String imageId;
    private String sex;
    private String home;
    private String school;
    private String aClass;
    private String phone;
    private String email;
    private String birthday;
    public Friends(String ip,String netname,String geqian,String  imageId,String sex,String home,String school,String aClass,String phone,String email,String birthday){
        this.ip=ip;
        this.netname=netname;
        this.geqian=geqian;
        this.imageId=imageId;
        this.sex=sex;
        this.home=home;
        this.school=school;
        this.aClass=aClass;
        this.email=email;
        this.birthday=birthday;
        this.phone=phone;
    }
    public String getip(){
        return ip;
    }
    public String getNetname(){
        return netname;
    }
    public String getGeqian(){
        return geqian;
    }
    public String getImageId(){
        return imageId;
    }
    public String getSex(){
        return sex;
    }
    public String getHome(){
        return home;
    }
    public String getPhone(){
        return phone;
    }
    public String getSchool(){
        return school;
    }
    public String getEmail(){
        return email;
    }
    public String getBirthday(){
        return birthday;
    }
    public String getaClass(){
        return  aClass;
    }
}
