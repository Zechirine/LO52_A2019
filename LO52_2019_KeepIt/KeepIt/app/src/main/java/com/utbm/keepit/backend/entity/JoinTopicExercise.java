package com.utbm.keepit.backend.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class JoinTopicExercise {
    private Long topicId;
    private Long exerciseId;
    @Generated(hash = 856594949)
    public JoinTopicExercise(Long topicId, Long exerciseId) {
        this.topicId = topicId;
        this.exerciseId = exerciseId;
    }
    @Generated(hash = 1733256184)
    public JoinTopicExercise() {
    }
    public Long getTopicId() {
        return this.topicId;
    }
    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
    public Long getExerciseId() {
        return this.exerciseId;
    }
    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }
}
