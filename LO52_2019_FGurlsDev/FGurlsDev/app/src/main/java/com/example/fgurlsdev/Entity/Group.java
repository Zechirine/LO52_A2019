package com.example.fgurlsdev.Entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;
import org.greenrobot.greendao.DaoException;

@Entity
public class Group {
    @Id(autoincrement = true)
    private Long groupId;
    @Property(nameInDb = "groupName")
    @Unique
    private String groupName;
    @Property(nameInDb = "runnerId1")
    private Long runnerId1;
    @Property(nameInDb = "runnerId2")
    private Long runnerId2;
    @Property(nameInDb = "runnerId3")
    private Long runnerId3;
    @Property(nameInDb = "nbMembers")
    private int nbMembers;

    @Generated(hash = 145843332)
    public Group(Long groupId, String groupName, Long runnerId1, Long runnerId2,
            Long runnerId3, int nbMembers) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.runnerId1 = runnerId1;
        this.runnerId2 = runnerId2;
        this.runnerId3 = runnerId3;
        this.nbMembers = nbMembers;
    }
    @Generated(hash = 117982048)
    public Group() {
    }
    public Long getGroupId() {
        return this.groupId;
    }
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public Long getRunnerId1() {
        return this.runnerId1;
    }
    public void setRunnerId1(Long runnerId1) {
        this.runnerId1 = runnerId1;
    }
    public Long getRunnerId2() {
        return this.runnerId2;
    }
    public void setRunnerId2(Long runnerId2) {
        this.runnerId2 = runnerId2;
    }
    public Long getRunnerId3() {
        return this.runnerId3;
    }
    public void setRunnerId3(Long runnerId3) {
        this.runnerId3 = runnerId3;
    }
    public int getNbMembers() {
        return this.nbMembers;
    }
    public void setNbMembers(int nbMembers) {
        this.nbMembers = nbMembers;
    }
}
