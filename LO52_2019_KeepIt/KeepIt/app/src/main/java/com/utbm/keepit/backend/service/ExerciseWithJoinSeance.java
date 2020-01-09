package com.utbm.keepit.backend.service;

import android.graphics.Paint;

import com.utbm.keepit.backend.entity.Exercise;
import com.utbm.keepit.backend.entity.JoinSeanceExercise;

public class ExerciseWithJoinSeance {
    public Exercise e;
    public JoinSeanceExercise jse;

    public ExerciseWithJoinSeance(Exercise e, JoinSeanceExercise jse){
        this.e = e;
        this.jse=jse;
    }
    public Exercise getExercise(){
        return this.e;
    }

    public JoinSeanceExercise getJSE(){
        return this.jse;
    }
}
