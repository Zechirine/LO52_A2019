package com.utbm.keepit.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utbm.keepit.R;
import com.utbm.keepit.activities.ExerciceActivity;
import com.utbm.keepit.backend.entity.Topic;

import java.util.List;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {
    private Context context;
    private List<Topic> topics;
    public TopicListAdapter(Context context, List<Topic> topics){
        this.context=context;
        this.topics=topics;
//        for(Topic t: this.topics){
//            System.out.println(t.toString());
//        }

    }
    @NonNull
    @Override
    public TopicListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.topic_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopicListAdapter.ViewHolder holder, final int position) {
            if(topics.get(position).getImagePath()!= null){
                //  Uri imageUri = Uri.parse((String) str);
                holder.imageView.setImageURI( Uri.parse( topics.get(position).getImagePath()));
            }else{
                holder.imageView.setImageResource(R.mipmap.dos);
//            android:src="@mipmap/dos"
            }
        holder.testView.setText(topics.get(position).getTopicName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ExerciceActivity.class);
                intent.putExtra("topicid",topics.get(position).getId());//TODO 根据topicid获取对应的exercice
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView testView;
        View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView=itemView;

            imageView=itemView.findViewById(R.id.img_topic);
            testView = itemView.findViewById(R.id.name_topic);

        }
    }
}
