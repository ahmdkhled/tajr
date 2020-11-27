package com.tajr.tajr.adapters;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tajr.tajr.R;
import com.tajr.tajr.models.OrderStatusHistoryResponse;

import java.util.List;

public class StatusHistoryAdapter extends RecyclerView.Adapter<StatusHistoryAdapter.Holder> {

    List<OrderStatusHistoryResponse.History> historyList;
    Activity context;

    public StatusHistoryAdapter(List<OrderStatusHistoryResponse.History> historyList, Activity context) {
        this.historyList = historyList;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_status_history, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        OrderStatusHistoryResponse.History history = historyList.get(i);
        holder.source.setText(history.getSource());
        holder.date.setText(history.getDate());
        holder.status.setText(history.getStatus());

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView status, date, source;

        public Holder(@NonNull View itemView) {
            super(itemView);

            source = itemView.findViewById(R.id.source);
            status = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.date);
        }
    }
}
