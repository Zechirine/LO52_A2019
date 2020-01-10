package com.example.fgurlsdev.Entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Competition {
    @Id(autoincrement = true)
    private Long competitionId;
    @Property(nameInDb = "competitionName")
    private String competitionName;
    @Property(nameInDb = "nbGroups")
    private int nbGroups;
    @Property(nameInDb = "competitionDate")
    private String competitionDate;

    @Generated(hash = 749653627)
    public Competition(Long competitionId, String competitionName, int nbGroups,
            String competitionDate) {
        this.competitionId = competitionId;
        this.competitionName = competitionName;
        this.nbGroups = nbGroups;
        this.competitionDate = competitionDate;
    }
    @Generated(hash = 61334593)
    public Competition() {
    }
    public Long getCompetitionId() {
        return this.competitionId;
    }
    public void setCompetitionId(Long competitionId) {
        this.competitionId = competitionId;
    }
    public String getCompetitionName() {
        return this.competitionName;
    }
    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }
    public int getNbGroups() {
        return this.nbGroups;
    }
    public void setNbGroups(int nbGroups) {
        this.nbGroups = nbGroups;
    }
    public String getCompetitionDate() {
        return this.competitionDate;
    }
    public void setCompetitionDate(String competitionDate) {
        this.competitionDate = competitionDate;
    }
}
