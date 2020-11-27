package com.tajr.tajr.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tajr.tajr.R;
import com.tajr.tajr.models.AdminRecordsResponse;
import com.rygelouv.audiosensei.player.AudioSenseiPlayerView;

import java.util.List;

public class AdminRecordsAdapter extends RecyclerView.Adapter<AdminRecordsAdapter.Holder>{

    List<AdminRecordsResponse.Record> records;
    Context context;

    public AdminRecordsAdapter(List<AdminRecordsResponse.Record> records, Context context) {
        this.records = records;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_admin_records_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        AdminRecordsResponse.Record record = records.get(i);

        holder.record.setAudioTarget(record.getLink());
        holder.name.setText(record.getUploder());
        holder.time.setText(record.getTime());

    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView name,time;
        AudioSenseiPlayerView record;
        public Holder(@NonNull View mainView) {
            super(mainView);

            name = mainView.findViewById(R.id.name);
            time = mainView.findViewById(R.id.time);
            record = mainView.findViewById(R.id.record);


        }
    }
}
