package com.tajr.tajr.adapters;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tajr.tajr.R;
import com.tajr.tajr.models.News;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    Activity context;
    List<News>newsList;

    public NewsAdapter(Activity context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_news_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int i) {
        News news = newsList.get(i);

        holder.message.setText(news.getMessage());
        holder.image.setImageURI(news.getImage());
        holder.name.setText(news.getName());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView image;
        TextView name;
        TextView message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);

        }
    }
}
