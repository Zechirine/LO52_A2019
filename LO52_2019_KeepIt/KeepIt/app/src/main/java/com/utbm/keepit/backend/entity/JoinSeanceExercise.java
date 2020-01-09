package com.utbm.keepit.backend.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class JoinSeanceExercise {
    @Id(autoincrement = true)
    private Long id;
    private Long seanceId;
    private Long exerciseId;
    private int duration;
    private int exerciseOrdre;
    @Generated(hash = 1309384651)
    public JoinSeanceExercise(Long id, Long seanceId, Long exerciseId, int duration,
            int exerciseOrdre) {
        this.id = id;
        this.seanceId = seanceId;
        this.exerciseId = exerciseId;
        this.duration = duration;
        this.exerciseOrdre = exerciseOrdre;
    }
    @Generated(hash = 992670927)
    public JoinSeanceExercise() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getSeanceId() {
        return this.seanceId;
    }
    public void setSeanceId(Long seanceId) {
        this.seanceId = seanceId;
    }
    public Long getExerciseId() {
        return this.exerciseId;
    }
    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }
    public int getDuration() {
        return this.duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public int getExerciseOrdre() {
        return this.exerciseOrdre;
    }
    public void setExerciseOrdre(int exerciseOrdre) {
        this.exerciseOrdre = exerciseOrdre;
    }
}
