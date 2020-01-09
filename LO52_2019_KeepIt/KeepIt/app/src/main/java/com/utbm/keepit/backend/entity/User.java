package com.utbm.keepit.backend.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {
    @Id
    private String name;

    private String pwd;

    @Generated(hash = 710494783)
    public User(String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return this.pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String toString(){
        return this.getName()+"    pwd:"+this.getPwd();
    }
}
