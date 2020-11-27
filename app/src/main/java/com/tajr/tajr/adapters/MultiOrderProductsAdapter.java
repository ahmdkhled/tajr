package com.tajr.tajr.adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tajr.tajr.R;
import com.tajr.tajr.helper.SessionManager;
import com.tajr.tajr.models.DeleteAddProductResponse;
import com.tajr.tajr.models.CurrentOrderData;
import com.tajr.tajr.enums.ResponseCodeEnums;
import com.tajr.tajr.models.MultiOrderProducts;
import com.tajr.tajr.server.Api;
import com.tajr.tajr.server.BaseClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tajr.tajr.view.dialogs.Dialogs.showProgressDialog;

public class MultiOrderProductsAdapter extends RecyclerView.Adapter<MultiOrderProductsAdapter.Holder> {

    private Context context;
    private List<MultiOrderProducts> proDucts;
    private GetOrderInterface getOrderInterface;

    public MultiOrderProductsAdapter(Context context, List<MultiOrderProducts> proDucts, GetOrderInterface getOrderInterface) {
        this.context = context;
        this.proDucts = proDucts;
        this.getOrderInterface = getOrderInterface;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_item_add, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {


        MultiOrderProducts proDuct = proDucts.get(i);
        holder.name.setText(proDuct.getProduct_name());
        holder.no.setText(proDuct.getItems_no());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getId(), String.valueOf(proDuct.getExtra_product_key()), proDuct.getProduct_id(), i);
            }
        });

    }

    private void showDeleteDialog(String orderId, String extra_product_key, String product_id, int position) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_delete_poduct_dialog);
        dialog.show();
        TextView deleteBtn = dialog.findViewById(R.id.delete_product_btn);

        deleteBtn.setOnClickListener(v -> {
            deleteProduct(orderId, extra_product_key, product_id, position);
            dialog.dismiss();
        });

        dialog.findViewById(R.id.cancel_btn).setOnClickListener(v -> dialog.dismiss());

    }

    @Override
    public int getItemCount() {
        return proDucts.size();
    }

    private void deleteProduct(String orderId, String extra_product_key, String product_id, int position) {
        ProgressDialog progressDialog = showProgressDialog(context, context.getString(R.string.delete_product));
        Api api = BaseClient.getBaseClient().create(Api.class);

        api.deleteProduct(
                SessionManager.getInstance(context).getToken(),
                orderId,
                extra_product_key,
                CurrentOrderData.getInstance().getCurrentOrderResponse().getUserId(),
                product_id
        ).enqueue(new Callback<DeleteAddProductResponse>() {
            @Override
            public void onResponse(Call<DeleteAddProductResponse> call, Response<DeleteAddProductResponse> response) {
                progressDialog.dismiss();
                if (response.body().getCode().equals(ResponseCodeEnums.code_1200.getCode())) {
                    proDucts.remove(position);

                    notifyDataSetChanged();
                    if (proDucts.size() == 1) {
                        getOrderInterface.getOrder();
                    }

                }
                Toast.makeText(context, "" + response.body().getDetails(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DeleteAddProductResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public interface GetOrderInterface {

        void getOrder();
    }

    public List<MultiOrderProducts> getProDucts() {
        return proDucts;
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView name, no;
        ImageView delete;

        public Holder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.product_name);
            no = itemView.findViewById(R.id.items_no);
            delete = itemView.findViewById(R.id.delete);

        }
    }


}
