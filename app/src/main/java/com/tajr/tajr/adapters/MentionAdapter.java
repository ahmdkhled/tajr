package com.tajr.tajr.adapters;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tajr.tajr.R;
import com.tajr.tajr.models.User;
import com.rafakob.drawme.DrawMeTextView;

import java.util.ArrayList;
import java.util.List;

public class MentionAdapter extends RecyclerView.Adapter<MentionAdapter.ViewHolder> {

    List<User> users;
    List<User> usersCopy;
    Activity context;

    public MentionAdapter(List<User> users, Activity context) {
        this.users = users;
        this.usersCopy = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_user_mention_layout,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        User user = users.get(i);
        viewHolder.name.setText(user.getName());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        DrawMeTextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name);
        }
    }

    public void addUsers(User user){
        users.add(user);
        usersCopy.add(user);
        notifyDataSetChanged();
    }

    public void filter(String text) {
        users.clear();
        if(text.isEmpty()){
            users.addAll(usersCopy);
        } else{
            text = text.toLowerCase();
            for(User item: usersCopy){
                if(item.getName().toLowerCase().contains(text) ){
                    users.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
