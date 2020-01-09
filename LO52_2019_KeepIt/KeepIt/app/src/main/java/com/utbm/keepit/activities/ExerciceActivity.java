package com.utbm.keepit.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.utbm.keepit.R;
import com.utbm.keepit.backend.entity.Exercise;
import com.utbm.keepit.backend.service.ExerciseService;
import com.utbm.keepit.ui.ExerciceListAdapter;

import java.util.List;

public class ExerciceActivity extends AppCompatActivity {
    private RecyclerView ExerciceList;
    private Button button;
    private Long tid;
    private ExerciceListAdapter exerciceListAdapter;
    private ExerciseService exerciseService = new ExerciseService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice);
        tid=getIntent().getExtras().getLong("topicid");
        button=findViewById(R.id.btn_create_exercise);
        List<Exercise> listExerciceData = exerciseService.findByTopicId(tid);//TODO : exercice数据源 根据homefragment传过来的id
        ExerciceList=findViewById(R.id.exercise_list);

//        rvTopic.addItemDecoration(new GridSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.marginItemSize),rvGrid));

        exerciceListAdapter =new ExerciceListAdapter(this,listExerciceData);
        ExerciceList.setLayoutManager(new LinearLayoutManager(this));
        ExerciceList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        ExerciceList.setAdapter(exerciceListAdapter);
    }
    public void onCreateExercise(View v){
        Intent intent=new Intent(this,CreateExerciseActivity.class);
        startActivity(intent);
    }
}
