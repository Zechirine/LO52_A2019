package com.example.fgurlsdev.Entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class Runner {
    @Id(autoincrement = true)
    private Long runnerId;
    @Property(nameInDb = "runnerName")
    @Unique
    private String nom;
    @Property(nameInDb = "hasGroup")
    private boolean hasGroup;

    @Generated(hash = 122379128)
    public Runner(Long runnerId, String nom, boolean hasGroup) {
        this.runnerId = runnerId;
        this.nom = nom;
        this.hasGroup = hasGroup;
    }

    @Generated(hash = 1499290990)
    public Runner() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Long getRunnerId() {
        return runnerId;
    }

    public void setRunnerId(Long runnerId) {
        this.runnerId = runnerId;
    }

    public boolean getHasGroup() {
        return this.hasGroup;
    }

    public void setHasGroup(boolean hasGroup) {
        this.hasGroup = hasGroup;
    }
}
