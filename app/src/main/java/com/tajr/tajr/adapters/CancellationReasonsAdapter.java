package com.tajr.tajr.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tajr.tajr.R;
import com.tajr.tajr.models.CancellationReason;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CancellationReasonsAdapter  extends RecyclerView.Adapter<CancellationReasonsAdapter.CancellationReasonsViewHolder> {

    private ArrayList<CancellationReason> cancellationReasons;
    private OnReasonSelected onReasonSelected;


    public CancellationReasonsAdapter(ArrayList<CancellationReason> cancellationReasons, OnReasonSelected onReasonSelected) {
        this.cancellationReasons = cancellationReasons;
        this.onReasonSelected = onReasonSelected;
    }

    @NonNull
    @Override
    public CancellationReasonsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cancel_reason,parent,false);
        return new CancellationReasonsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CancellationReasonsViewHolder holder, int position) {
        holder.reason.setText(cancellationReasons.get(position).getReason_name());
    }

    @Override
    public int getItemCount() {
        return cancellationReasons==null?0:cancellationReasons.size();
    }

    class CancellationReasonsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reason)
        TextView reason;
        CancellationReasonsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id=Integer.valueOf(cancellationReasons.get(getAdapterPosition()).getReason_id());
                    onReasonSelected.onReasonSelected(id);
                }
            });
        }
    }

    public interface OnReasonSelected{
        void onReasonSelected(int reason);
    }
}
