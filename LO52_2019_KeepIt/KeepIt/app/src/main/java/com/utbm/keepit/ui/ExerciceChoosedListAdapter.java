package com.utbm.keepit.ui;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utbm.keepit.R;
import com.utbm.keepit.backend.entity.Exercise;
import com.utbm.keepit.backend.entity.ExerciseDataToDesciption;
import com.utbm.keepit.backend.entity.JoinSeanceExercise;

import java.util.List;

public class ExerciceChoosedListAdapter extends RecyclerView.Adapter<ExerciceChoosedListAdapter.ViewHolder> {
    private Context context;
    private List<Exercise> exercises;
    private List<JoinSeanceExercise> tempSeanceExercise;


    public ExerciceChoosedListAdapter(Context context, List<Exercise> exercises,
                                      List<JoinSeanceExercise> tempSeanceExercise){
        this.context=context;
        this.exercises=exercises;
        this.tempSeanceExercise=tempSeanceExercise;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExerciceChoosedListAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.exercise_choosed_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        System.out.println(exercises.get(position).toString());
//        System.out.println(exercises.get(position).getImageResource().toString());
        if (exercises.get(position).getImageResource() == null) {
            //  Uri imageUri = Uri.parse((String) str);
            holder.imageView.setImageResource(R.mipmap.muscle);

        } else if (exercises.get(position).getImageResource() == "" || exercises.get(position).getImageResource() == " ") {
            holder.imageView.setImageResource(R.mipmap.dos);
        } else {
            holder.imageView.setImageURI(Uri.parse(exercises.get(position).getImageResource()));
//            android:src="@mipmap/dos"
        }
        holder.exercise_name.setText(exercises.get(position).getName());
        holder.exercise_public.setText("Type:" + ExerciseDataToDesciption.descripPublic.get(exercises.get(position).getTypePublic()));
        holder.exercise_level.setText("Level:" + ExerciseDataToDesciption.descripGroup.get(exercises.get(position).getLevelGroup()));
        holder.exercise_diff.setText("Difficulté:" + ExerciseDataToDesciption.descripDifficult.get(exercises.get(position).getLevelDifficult()));
        int time = tempSeanceExercise.get(position).getDuration();
        int h = time / 3600;
        int m = time % 3600 / 60;
        int s = time - h * 3600 - m * 60;
        String hs = "";
        String ms = "";
        String ss = "";
        if (h != 0) {
            hs = h + "H ";
        }
        if (m != 0) {
            ms = m + "M ";
        }
        if (s != 0){
            ss = s + "S";
        }
        holder.duration.setText("Duration:"+hs+ms+ss);
        }


    @Override
    public int getItemCount() {
        return exercises.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView exercise_name,exercise_public,exercise_level,exercise_diff,duration;
        View itemView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView=itemView;
            imageView=itemView.findViewById(R.id.exercise_choosed_image);
            exercise_name=itemView.findViewById(R.id.exercise_choosed_name);
            exercise_public=itemView.findViewById(R.id.exercise__choosed_public);
            exercise_level=itemView.findViewById(R.id.exercise_choosed_level);
            exercise_diff=itemView.findViewById(R.id.exercise__choosed_diff);
            duration =itemView.findViewById(R.id.duration);
        }
    }
}
