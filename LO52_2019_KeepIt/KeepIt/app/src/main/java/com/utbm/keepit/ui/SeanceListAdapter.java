package com.utbm.keepit.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utbm.keepit.R;
import com.utbm.keepit.activities.AfficherSeanceExerciseActivity;
import com.utbm.keepit.backend.entity.Seance;

import java.util.List;

public class SeanceListAdapter extends RecyclerView.Adapter<SeanceListAdapter.ViewHolder> {
    private Context context;
    private List<Seance> seances;

    public SeanceListAdapter(Context context,List<Seance> seances) {
        this.context=context;
        this.seances = seances;
        for(Seance s: this.seances){
            System.out.println(s.getId()+" "+s.getName());
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.seance_item,parent,false));

    }

    //TODO:
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(seances.get(position).getName());
        holder.duration.setText("Duration:"+seances.get(position).getDuration());
        holder.intensity.setText("Intensity:"+seances.get(position).getIntensity().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AfficherSeanceExerciseActivity.class);
                intent.putExtra("seanceid",seances.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return seances.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,intensity,duration;
        View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView=itemView;
            name=itemView.findViewById(R.id.seance_name);
            intensity=itemView.findViewById(R.id.seance_intensity);
            duration = itemView.findViewById(R.id.seance_duration);

        }
    }
}
