package com.utbm.keepit.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.utbm.keepit.R;
import com.utbm.keepit.backend.entity.Seance;
import com.utbm.keepit.backend.service.SeanceService;
import com.utbm.keepit.ui.SeanceListAdapter;

import java.util.List;

public class SeanceListActivity extends AppCompatActivity {
    private SeanceListAdapter seanceListAdapter;
    private RecyclerView seanceList;
    private SeanceService seanceService = new SeanceService();
    private List<Seance> seances;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seance_list);
        seanceList=findViewById(R.id.seance_list);
        seances=seanceService.findAll();
        seanceListAdapter =new SeanceListAdapter(this,seances);
        seanceList.setLayoutManager(new LinearLayoutManager(this));
        seanceList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        seanceList.setAdapter(seanceListAdapter);
    }
}
