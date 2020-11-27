package com.tajr.tajr.adapters;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tajr.tajr.R;
import com.tajr.tajr.models.ExtraData;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExtraDataAdapter extends RecyclerView.Adapter<ExtraDataAdapter.ExtraDetailViewHolder> {

    private Context context;
    private ArrayList<ExtraData> extraData;



    public ExtraDataAdapter(Context context, ArrayList<ExtraData> extraData) {
        this.context = context;
        this.extraData = extraData;
    }

    @NonNull
    @Override
    public ExtraDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.extra_data_layout,parent,false);
        return new ExtraDetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExtraDetailViewHolder holder, int position) {
        ExtraData extra=extraData.get(position);
        handleInputs(holder,extra);
    }

    @Override
    public int getItemCount() {
        return extraData==null?0:extraData.size();
    }



    private void handleInputs(ExtraDetailViewHolder holder, ExtraData extra){
        TextView label=holder.label;
        Spinner spinner=holder.spinner;
        EditText edittext=holder.value;


        label.setText(extra.getName());

        if (extra.getType().equals("select")){
            String[] options=extra.getDetails().split(",");
            ArrayList<String> items=new ArrayList<>();
            items.add("choose");
            items.addAll(Arrays.asList(options));
            ArrayAdapter arrayAdapter=new ArrayAdapter(context,android.R.layout.simple_list_item_1,items);
            spinner.setAdapter(arrayAdapter);

            spinner.setVisibility(View.VISIBLE);
            edittext.setVisibility(View.INVISIBLE);

            return;
        }

        spinner.setVisibility(View.INVISIBLE);
        edittext.setVisibility(View.VISIBLE);

        edittext.setHint(extra.getDetails());

        if (extra.getType().equals("number"))
                edittext.setInputType(InputType.TYPE_CLASS_NUMBER);

    }

    public ArrayList<ExtraData> getExtraData() {
        return extraData;
    }

    class ExtraDetailViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.value)
        EditText value;
        @BindView(R.id.spinnerValue)
        Spinner spinner;
        @BindView(R.id.field_label)
        TextView label;
        ExtraDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
