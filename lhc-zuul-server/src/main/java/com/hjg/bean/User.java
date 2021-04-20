package com.hjg.bean;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer uid;
    private String username;
    private String nickname;
    private String password;
    private Integer status;
    private Date onlineTime;
    private Date updateTime;
    private Date createTime;

    public User(){}

    public User(Integer uid, String username, String nickname, String password, Integer status, Date onlineTime, Date updateTime, Date createTime) {
        this.uid = uid;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.status = status;
        this.onlineTime = onlineTime;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }
}
