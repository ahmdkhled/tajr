package com.tajr.tajr.view.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tajr.tajr.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateVersionDialog extends DialogFragment {

    @BindView(R.id.update)
    Button update;
    @BindView(R.id.later)
    Button later;
    @BindView(R.id.body)
    TextView body;



    public UpdateVersionDialog() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialog=inflater.inflate(R.layout.update_version_dialog,container,false);
        ButterKnife.bind(this,dialog);
        setCancelable(false);

        later.setOnClickListener(view -> {
            this.dismiss();
        });

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
