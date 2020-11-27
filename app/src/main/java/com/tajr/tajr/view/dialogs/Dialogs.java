package com.tajr.tajr.view.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.tajr.tajr.R;
import com.tajr.tajr.helper.font.RobotoTextView;

public class Dialogs {

    public static Dialog showCustomDialog(Context activity, String msg, String title,
                                          String positive, String negative,
                                          View.OnClickListener positiveLisn, View.OnClickListener negativeLisn) {
        Dialog warningDialog = new Dialog(activity);
        warningDialog.setCancelable(false);
        warningDialog.setContentView(R.layout.custom_warning_dialog);
        RobotoTextView titleTv = warningDialog.findViewById(R.id.dialog_warning_title);
        RobotoTextView msgTc = warningDialog.findViewById(R.id.dialog_warning_msg);
        RobotoTextView okBtn = warningDialog.findViewById(R.id.positive);
        RobotoTextView cancelBtn = warningDialog.findViewById(R.id.negative);
        View divider = warningDialog.findViewById(R.id.divider);

        titleTv.setText(title);
        msgTc.setText(msg);

        okBtn.setText(positive);
        okBtn.setOnClickListener(positiveLisn);

        if (negative != null) {
            cancelBtn.setText(negative);
            cancelBtn.setOnClickListener(negativeLisn);
        } else {
            cancelBtn.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        }
        warningDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        warningDialog.show();

        return warningDialog;
    }

    public static ProgressDialog showProgressDialog(Context activity, String msg) {

        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }
}
