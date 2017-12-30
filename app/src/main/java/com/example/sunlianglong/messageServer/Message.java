package com.example.sunlianglong.messageServer;
/**
 * Created by sun liang long on 2017/10/20.
 */
//新建一个实体类，作为ListView的适配器的适配类型
//ListView      类中只有两个字段，name表示水果的名字，imageId表示水果对应图片的资源id。
public class Message {
    private String name;
    private int imageId2;
    private String geqian;
    public Message(String name, int imageId, String geqian){
        this.name=name;
        this.imageId2=imageId;
        this.geqian = geqian;
    }
    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId2;
    }
    public String getgeqian(){
        return geqian;
    }
}
