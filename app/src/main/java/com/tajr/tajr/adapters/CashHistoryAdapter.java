package com.tajr.tajr.adapters;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tajr.tajr.R;
import com.tajr.tajr.models.CashRequestHistory;

import java.util.List;

public class CashHistoryAdapter extends RecyclerView.Adapter<CashHistoryAdapter.ViewHolder> {

    List<CashRequestHistory.RequestData> cashDataList;
    Activity context;

    public CashHistoryAdapter(List<CashRequestHistory.RequestData > monthPoints, Activity context) {
        this.cashDataList = monthPoints;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_cash_requests_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        CashRequestHistory.RequestData  cashData = cashDataList.get(i);

        holder.time.setText(cashData.getTime());
        holder.cash.setText(cashData.getCash());
        holder.phone.setText(cashData.getMobile());
        holder.status.setText(cashData.getRequest_status());
        holder.notes.setText(cashData.getNotes());

    }

    @Override
    public int getItemCount() {
        return cashDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView phone;
        TextView notes;
        TextView cash;
        TextView time;
        TextView status;

        View mainView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mainView = itemView;
            phone = mainView.findViewById(R.id.phone);
            cash = mainView.findViewById(R.id.cash);
            time = mainView.findViewById(R.id.time);
            notes = mainView.findViewById(R.id.notes);
            status = mainView.findViewById(R.id.status);
        }
    }
}
