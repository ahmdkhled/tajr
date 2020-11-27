package com.tajr.tajr.helper;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.tajr.tajr.R;
import com.tajr.tajr.adapters.ExtraDataAdapter2;
import com.tajr.tajr.models.ProductExtra;

import java.util.ArrayList;
import java.util.HashMap;

public class ExtraDataHelper {

    public static HashMap<String, Object> getValues(Context context, ExtraDataAdapter2 extraDataAdapter, RecyclerView extraDataRecycler){
        HashMap<String,Object> values=new HashMap<>();

        ArrayList<ProductExtra> extraData=extraDataAdapter.getExtraData();
     for (int i = 0; i < extraDataAdapter.getItemCount(); i++) {



        View view = extraDataRecycler.getChildAt(i);
        if (Boolean.valueOf(extraData.get(i).Is_list())){
            Spinner spinner=view.findViewById(R.id.spinnerValue);

            if (Boolean.valueOf(extraData.get(i).getRequired())&&spinner.getSelectedItemPosition()==0){
                Toast.makeText(context, R.string.complete_fields_error, Toast.LENGTH_SHORT).show();
                return null;
            }

            if (spinner.getSelectedItemPosition()>0){
                String value=extraData.get(i).getList().get(spinner.getSelectedItemPosition());
                values.put(extraData.get(i).getHtml(),value);
            }

        }else {

            EditText editText=view.findViewById(R.id.value);
            String value=editText.getText().toString();
            if (Boolean.valueOf(extraData.get(i).getRequired())&& TextUtils.isEmpty(value)){
                Toast.makeText(context, R.string.complete_fields_error, Toast.LENGTH_SHORT).show();
                return null;
            }
            values.put(extraData.get(i).getHtml(),value);
        }

    }
        return values;
    }
}

