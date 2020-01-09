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

import java.util.List;

public class StringListAdapter extends RecyclerView.Adapter<StringListAdapter.ViewHolder> {
    private Context context;
    private List<String> strings;

    public StringListAdapter(Context context, List<String> strings){
        this.context=context;
        this.strings=strings;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StringListAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.chosed_topics_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.stringToShow.setText(strings.get(position));
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView stringToShow;
        View itemView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView=itemView;
            stringToShow =itemView.findViewById(R.id.string_to_show);
        }
    }
}
