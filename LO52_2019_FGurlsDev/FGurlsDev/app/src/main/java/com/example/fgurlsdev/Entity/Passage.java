package com.example.fgurlsdev.Entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Passage {
    @Id(autoincrement = true)
    private Long psgId;
    @Property(nameInDb = "psgLabel")
    private String psgLabel;
    @Property(nameInDb = "psgOrder")
    private int psgOrder;

    @Generated(hash = 1990750813)
    public Passage(Long psgId, String psgLabel, int psgOrder) {
        this.psgId = psgId;
        this.psgLabel = psgLabel;
        this.psgOrder = psgOrder;
    }
    @Generated(hash = 1157322130)
    public Passage() {
    }
    public Long getPsgId() {
        return this.psgId;
    }
    public void setPsgId(Long psgId) {
        this.psgId = psgId;
    }
    public int getPsgOrder() {
        return this.psgOrder;
    }
    public void setPsgOrder(int psgOrder) {
        this.psgOrder = psgOrder;
    }
    public String getPsgLabel() {
        return this.psgLabel;
    }
    public void setPsgLabel(String psgLabel) {
        this.psgLabel = psgLabel;
    }
}
