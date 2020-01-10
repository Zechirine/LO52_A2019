package com.example.fgurlsdev.Entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class PassagePart {
    @Id(autoincrement = true)
    private Long psgPartId;
    @Property(nameInDb = "partLabel")
    private String partLabel;
    @Property(nameInDb = "runnerName")
    private String runnerName;
    @Property(nameInDb = "groupName")
    private String groupName;
    @Property(nameInDb = "partResult")
    private Long partResult;
    @Property(nameInDb = "competitionId")
    private Long competitionId;
    @Property(nameInDb = "psgOrder")
    private int psgOrder;

    @Generated(hash = 1028349325)
    public PassagePart(Long psgPartId, String partLabel, String runnerName,
            String groupName, Long partResult, Long competitionId, int psgOrder) {
        this.psgPartId = psgPartId;
        this.partLabel = partLabel;
        this.runnerName = runnerName;
        this.groupName = groupName;
        this.partResult = partResult;
        this.competitionId = competitionId;
        this.psgOrder = psgOrder;
    }
    @Generated(hash = 1088452038)
    public PassagePart() {
    }
    public Long getPsgPartId() {
        return this.psgPartId;
    }
    public void setPsgPartId(Long psgPartId) {
        this.psgPartId = psgPartId;
    }
    public String getPartLabel() {
        return this.partLabel;
    }
    public void setPartLabel(String partLabel) {
        this.partLabel = partLabel;
    }
    public String getRunnerName() {
        return this.runnerName;
    }
    public void setRunnerName(String runnerName) {
        this.runnerName = runnerName;
    }
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public Long getPartResult() {
        return this.partResult;
    }
    public void setPartResult(Long partResult) {
        this.partResult = partResult;
    }
    public Long getCompetitionId() {
        return this.competitionId;
    }
    public void setCompetitionId(Long competitionId) {
        this.competitionId = competitionId;
    }
    public int getPsgOrder() {
        return this.psgOrder;
    }
    public void setPsgOrder(int psgOrder) {
        this.psgOrder = psgOrder;
    }
}
