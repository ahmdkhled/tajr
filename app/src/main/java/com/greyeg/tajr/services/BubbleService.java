package com.greyeg.tajr.services;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greyeg.tajr.R;
import com.greyeg.tajr.activities.LoginActivity;
import com.greyeg.tajr.adapters.BotBlocksAdapter;
import com.greyeg.tajr.adapters.CartAdapter;
import com.greyeg.tajr.adapters.ExtraDataAdapter2;
import com.greyeg.tajr.adapters.ProductAdapter;
import com.greyeg.tajr.adapters.ProductSpinnerAdapter;
import com.greyeg.tajr.adapters.SubscribersAdapter;
import com.greyeg.tajr.helper.EndlessRecyclerViewScrollListener;
import com.greyeg.tajr.helper.ExtraDataHelper;
import com.greyeg.tajr.helper.ScreenHelper;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.helper.UserNameEvent;
import com.greyeg.tajr.models.AllProducts;
import com.greyeg.tajr.models.BotBlock;
import com.greyeg.tajr.models.BotBlocksResponse;
import com.greyeg.tajr.models.Broadcast;
import com.greyeg.tajr.models.CartItem;
import com.greyeg.tajr.models.Cities;
import com.greyeg.tajr.models.NewOrderResponse;
import com.greyeg.tajr.models.OrderItem;
import com.greyeg.tajr.models.OrderPayload;
import com.greyeg.tajr.models.Pages;
import com.greyeg.tajr.models.ProductData;
import com.greyeg.tajr.models.ProductForSpinner;
import com.greyeg.tajr.models.Subscriber;
import com.greyeg.tajr.models.SubscriberInfo;
import com.greyeg.tajr.repository.CitiesRepo;
import com.greyeg.tajr.repository.OrdersRepo;
import com.greyeg.tajr.repository.ProductsRepo;
import com.greyeg.tajr.server.BaseClient;
import com.rafakob.drawme.DrawMeButton;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BubbleService extends Service
        implements BotBlocksAdapter.OnBlockSelected
            , SubscribersAdapter.OnSubscriberSelected
            , CartAdapter.OnCartItemEvent
            , ProductAdapter.OnProductClicked{

    private WindowManager mWindowManager;
    private View bubbleView;
    private View deleteView;
    View expandedView;
    View botBlocksDialog;
    View subscribersDialog;
    View newOrderDialog;
    View addNewProductDialog;
    WindowManager.LayoutParams params;
    WindowManager.LayoutParams dialogParams;
    WindowManager.LayoutParams subscribersDialogParams;
    WindowManager.LayoutParams newOrderDialogParams;
    public  String  userName=null;
    public  String psid =null;
    public  String page=null;
    private String userId;
    public  String blockId=null;
    private static final int CLICK_ACTION_THRESHOLD = 0;
    private float startX;
    private float startY;
    public static boolean isRunning=false;
    private   boolean deleteViewAdded=false;
    private Handler handler;
    private int width,height;
    List<String> cities = new ArrayList<>();
    public static String CITY_ID;
    List<String> citiesId = new ArrayList<>();
    private List<Cities.City> citiesBody;
    ArrayList<Subscriber> subscribers=new ArrayList<>();
    ArrayList<OrderItem> orderItems;
    CartAdapter cartAdapter;
    ExtraDataAdapter2 extraDataAdapter;
    private ProductAdapter productAdapter;
    RecyclerView extraDataRecycler;
    private ViewGroup emptyView;
    private Pages pages;
    private ProgressBar products_PB;
    private TextView productName,productPrice;
    private ImageView productImage;
    public static boolean isLoading;
    OrderItem orderItem;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning=true;
        EventBus.getDefault().register(this);

        bubbleView = LayoutInflater.from(this).inflate(R.layout.bubble, null,false);
        deleteView = LayoutInflater.from(this).inflate(R.layout.bubble_delete, null,false);
        View collapsedView=bubbleView.findViewById(R.id.collapsed_bubble);
        expandedView=bubbleView.findViewById(R.id.expanded_bubble);

        width= ScreenHelper.getScreenDimensions(getApplicationContext())[0];
        height= ScreenHelper.getScreenDimensions(getApplicationContext())[1];
        orderItems=new ArrayList<>();
        cartAdapter=new CartAdapter(getApplicationContext(),this,true);

        Log.d("SCREEEEEENw", "onCreate: "+width);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }else{
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(bubbleView, params);


        collapsedView.setOnTouchListener(onTouchListener);

        Log.d("SCREEEN", "width: "+ width+"     "+height);

        bubbleView.findViewById(R.id.send_broadcast)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getBotBlocks();
                        Toast.makeText(BubbleService.this, "sending broadcast", Toast.LENGTH_SHORT).show();
                    }
                });

        bubbleView.findViewById(R.id.register_order)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setupNewOrderDialog();
                        bubbleView.setVisibility(View.GONE);

                    }
                });



    }



    @Subscribe()
    public void onMessageEvent(UserNameEvent event) {
        if (event.getUserNmae()!=null){
            Log.d("EVENTTT", event.getUserNmae()+" -------> : "+userName);
            if (userName == null || !userName.equals(event.getUserNmae())){
                psid =null;
                getUserId(event.getUserNmae());
                Log.d("EVENTTT","getting userName");

            }

            userName=event.getUserNmae();
            Log.d("EVENTTT","set userName "+userName);

        }
        if (bubbleView==null)
            return;

        if (event.getUserNmae()!=null)
            bubbleView.findViewById(R.id.gotUserName)
            .setBackgroundResource(R.drawable.circle_green);
        else
            bubbleView.findViewById(R.id.gotUserName)
                    .setBackgroundResource(R.drawable.circle_gray);

    };


    private void getUserId(String userName){
        subscribers.clear();
        String token= SharedHelper.getKey(getApplicationContext(), LoginActivity.TOKEN);
        Log.d("SUBSCRIPERR", "token: "+token);
        startFlasher();
        BaseClient.getApiService()
                .getSubscriberInfo(token,userName)
                .enqueue(new Callback<SubscriberInfo>() {
                    @Override
                    public void onResponse(Call<SubscriberInfo> call, Response<SubscriberInfo> response) {
                        stopFlasher();
                        SubscriberInfo subscriberInfo=response.body();
                        if (response.isSuccessful()&&subscriberInfo!=null){
                            Log.d("SUBSCRIPERR","subscriper "+subscriberInfo.getData());
                            ArrayList<Subscriber> subscribersData =subscriberInfo.getSubscribers_data();
                            if(subscribersData==null||subscribersData.isEmpty()){
                                //todo handle session expire
                                stopFlasher();
                                Toast.makeText(BubbleService.this,
                                        subscriberInfo.getData()
                                        , Toast.LENGTH_LONG).show();
                            }else if (subscribersData.size()==1){
                                psid =subscribersData.get(0).getPsid();
                                page=subscribersData.get(0).getPage();
                                userId=subscribersData.get(0).getId();

                            }else {
                                subscribers=subscribersData;
                            }

                        }
                        else {
                            Toast.makeText(BubbleService.this, "Server Error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SubscriberInfo> call, Throwable t) {
                        stopFlasher();
                        Log.d("SUBSCRIPERR", "onFailure: "+t.getMessage());
                    }
                });
    }

    private void getBotBlocks(){
        setBroadCastLoading(View.VISIBLE);
        String token=SharedHelper.getKey(getApplicationContext(),LoginActivity.TOKEN);

        BaseClient.getApiService()
                .getBotBlocks(token)
                .enqueue(new Callback<BotBlocksResponse>() {
                    @Override
                    public void onResponse(Call<BotBlocksResponse> call, Response<BotBlocksResponse> response) {
                        BotBlocksResponse botBlocksResponse=response.body();
                        if (response.isSuccessful()&&botBlocksResponse!=null){
                            setupBotBlocksDialog(botBlocksResponse);


                            //Log.d("BOTBLOKSS", "onResponse: "+botBlocksResponse.getBlocks().getDefault().get(0).getName());
                        }else{
                            Toast.makeText(BubbleService.this,
                                    "Error getting Bot Blocks \n response code "+response.code()
                                    , Toast.LENGTH_SHORT).show();
                        }
                        setBroadCastLoading(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<BotBlocksResponse> call, Throwable t) {
                        Log.d("BOTBLOKSS", "onFailure: "+t.getMessage());
                        Toast.makeText(BubbleService.this,
                                "failed to get Blocks \n "+t.getMessage()
                                , Toast.LENGTH_SHORT).show();
                        setBroadCastLoading(View.GONE);

                    }
                });
    }



    private void pasteData(ImageView pasteButton,EditText editText){
        pasteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData =clipboard.getPrimaryClip();
                if (clipData!=null&&clipData.getItemCount()>0)
                    editText.setText(clipboard.getPrimaryClip().getItemAt(0).getText());
                else
                    Toast.makeText(BubbleService.this, "no text copied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void MakeNewOrder(String client_name, String client_order_phone1
            , String client_area, String client_address){

        if (orderItems.isEmpty()){
            Toast.makeText(this, R.string.choose_product_first, Toast.LENGTH_SHORT).show();
            return;
        }

        String[] params={client_name,client_order_phone1,client_area,client_address,CITY_ID};
        for (String param : params) {
            if (TextUtils.isEmpty(param)) {
                Toast.makeText(this, R.string.complete_fields_error, Toast.LENGTH_SHORT).show();
                return;
            }

        }

        if(client_order_phone1.length()<11){
            Toast.makeText(this, R.string.invalid_ph_num, Toast.LENGTH_SHORT).show();
            return;
        }





        Log.d("ORDERRRR", "token : "+SharedHelper.getKey(getApplicationContext(),LoginActivity.TOKEN));
        Log.d("ORDERRRR", "client_name: "+client_name);
        Log.d("ORDERRRR", "client_order_phone1: "+client_order_phone1);
        Log.d("ORDERRRR", "client_area: "+client_area);
        Log.d("ORDERRRR", "client_address: "+client_address);
        Log.d("ORDERRRR", "CITY ID: "+CITY_ID);
        Log.d("ORDERRRR", "product id: "+orderItems.get(0).getProduct_id());

        String token=SharedHelper.getKey(getApplicationContext(),LoginActivity.TOKEN);


//        HashMap<String,Object> extras=new HashMap<>();
//        extras.put("extra_field_name_1","s");
//        extras.put("extra_field_name_2","test");



        OrderPayload orderPayload=new OrderPayload(token,null,client_name,client_order_phone1,
                CITY_ID,client_area,client_address,null,
                userName,userId,orderItems);


        Log.d("ORDERRRR", "user id:"+orderPayload.getSender_id());


        Log.d("OORRDDEERRR","order items size"+orderItems.size());
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(newOrderDialog.getWindowToken(), 0);

        OrdersRepo
                .getInstance()
                .makeNewOrder(orderPayload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<NewOrderResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<NewOrderResponse> response) {
                        NewOrderResponse newOrderResponse=response.body();
                        if (newOrderResponse!=null){
                            Toast.makeText(BubbleService.this, newOrderResponse.getData(), Toast.LENGTH_LONG).show();
                            clearOrderFields();
                        }
                        else{
                            Log.d("OORRDDEERRR","r)");
                            Toast.makeText(BubbleService.this,"Error placing Order :(" +
                                    ""+response.code() , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(BubbleService.this,R.string.error_placing_order, Toast.LENGTH_SHORT).show();

                    }
                });
    }




    private void setupSubscribersDialog(ArrayList<Subscriber> subscribers){
        subscribersDialog=LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.subscripers_dialog,null);
        subscribersDialogParams=getViewParams(100,200,600,600,subscribersDialogParams);
        mWindowManager.addView(subscribersDialog,subscribersDialogParams);

        RecyclerView recyclerView=subscribersDialog.findViewById(R.id.subscribers_recycler);
        SubscribersAdapter adapter=new SubscribersAdapter(
                getApplicationContext(),subscribers,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));




        subscribersDialog.findViewById(R.id.delete)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mWindowManager.removeView(subscribersDialog);
                    }
                });

        subscribersDialog.setOnTouchListener(subscribersdialogTouchListener);

    }

    private void setupBotBlocksDialog(BotBlocksResponse botBlocksResponse){
        botBlocksDialog=LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.bot_blocks_dialog,null);
        dialogParams=getViewParams(100,200,600,600,dialogParams);
        mWindowManager.addView(botBlocksDialog,dialogParams);
        showBotBlocks(botBlocksResponse.getBlocks().getDefault()
                ,botBlocksDialog.findViewById(R.id.default_blocks_recycler));
        showBotBlocks(botBlocksResponse.getBlocks().getNormal()
                ,botBlocksDialog.findViewById(R.id.normal_blocks_recycler));
        botBlocksDialog.findViewById(R.id.root).setOnTouchListener(dialogTouchListener);
        
        for(BotBlock botBlock:botBlocksResponse.getBlocks().getNormal()){
            Log.d("BLOOKKK", ": "+botBlock.getName());
        }
        
        botBlocksDialog.findViewById(R.id.close)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //mWindowManager.updateViewLayout(botBlocksDialog,getViewParams(0,0,0,0));
                        mWindowManager.removeView(botBlocksDialog);
                    }
                });

    }

    private void showBotBlocks(ArrayList<BotBlock> botBlocks, RecyclerView recyclerView){
        BotBlocksAdapter botBlocksAdapter=new BotBlocksAdapter(botBlocks,this);
        recyclerView.setAdapter(botBlocksAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getApplicationContext()
                ,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void setupNewOrderDialog(){
        int[] dimenssions=ScreenHelper.getScreenDimensions(getApplicationContext());
        newOrderDialog=LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.new_order_dialog,null);
        newOrderDialogParams=getViewParams(dimenssions[0]*1/5,0,dimenssions[0]*4/5,dimenssions[1]*2/3,newOrderDialogParams);
        mWindowManager.addView(newOrderDialog,newOrderDialogParams);

        emptyView =newOrderDialog.findViewById(R.id.emptyView);
        RecyclerView cartRecycler=newOrderDialog.findViewById(R.id.cart);
        cartRecycler.setAdapter(cartAdapter);
        cartRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false));

        if (orderItems!=null&&orderItems.isEmpty()){
            emptyView.setVisibility(View.VISIBLE);
        }

        newOrderDialog.findViewById(R.id.close)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mWindowManager.removeView(newOrderDialog);
                        bubbleView.setVisibility(View.VISIBLE);
                    }
                });
        newOrderDialog.findViewById(R.id.minimize)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View orderView=newOrderDialog.findViewById(R.id.order_view);
                        if (orderView.getVisibility()==View.VISIBLE){
                            orderView.setVisibility(View.GONE);
                            newOrderDialogParams.height=WindowManager.LayoutParams.WRAP_CONTENT;
                            newOrderDialogParams.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

                        }
                        else{
                            orderView.setVisibility(View.VISIBLE);
                            newOrderDialogParams.height=600;
                            newOrderDialogParams.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

                        }


                        newOrderDialog.setLayoutParams(newOrderDialogParams);
                        mWindowManager.updateViewLayout(newOrderDialog,newOrderDialogParams);

                    }
                });


        getCities2();

        Spinner city=newOrderDialog.findViewById(R.id.client_city);
        EditText client_name=newOrderDialog.findViewById(R.id.client_name);
        EditText client_address=newOrderDialog.findViewById(R.id.client_address);
        EditText client_area=newOrderDialog.findViewById(R.id.client_area);
        EditText client_order_phone1=newOrderDialog.findViewById(R.id.client_order_phone1);
        DrawMeButton send_order=newOrderDialog.findViewById(R.id.send_order);

        ImageView client_name_Paste=newOrderDialog.findViewById(R.id.client_name_paste);
        ImageView client_address_paste=newOrderDialog.findViewById(R.id.client_address_paste);
        ImageView client_area_paste=newOrderDialog.findViewById(R.id.client_area_paste);
        ImageView client_order_phone1_paste=newOrderDialog.findViewById(R.id.client_order_phone1_paste);


        addNewProductDialog=LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.layout_add_poduct_dialog,null,false);
        ImageView addProduct=newOrderDialog.findViewById(R.id.addNewProduct);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newOrderDialog!=null)newOrderDialog.setVisibility(View.INVISIBLE);
                WindowManager.LayoutParams layoutParams=getViewParams(-1,-1,-1,-1,null);
                layoutParams.gravity = Gravity.TOP | Gravity.END;

                mWindowManager.addView(addNewProductDialog,layoutParams);
                setupAddNewProductDialog();
            }
        });

        newOrderDialog.setOnTouchListener(setOnTOuchListener(newOrderDialogParams));

        send_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MakeNewOrder(
                        client_name.getText().toString(),
                        client_order_phone1.getText().toString(),
                        client_area.getText().toString(),
                        client_address.getText().toString()

                );
            }
        });

//        item_no.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.d("TEXTWATCHHEER", charSequence+" : "+i +"  "+i1+"  "+i2 );
//                try{
//                    int q=Integer.valueOf(charSequence.toString());
//                    if (q==0&&i==0){
//                                Toast.makeText(BubbleService.this, R.string.enter_valid_quantity, Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    if (orderItems!=null&&!orderItems.isEmpty())
//                    cartAdapter.updateQuantity(orderItems.get(0).getProduct_id(),q);
//                }catch (Exception e){
//                    Log.d("TEXTWATCHHEER",e.getMessage());
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });


        pasteData(client_name_Paste,client_name);
        pasteData(client_address_paste,client_address);
        pasteData(client_area_paste,client_area);
        pasteData(client_order_phone1_paste,client_order_phone1);


    }

    private void setupAddNewProductDialog(){

        orderItem=new OrderItem();
        RecyclerView products_recycler=addNewProductDialog.findViewById(R.id.products_recycler);
        products_PB=addNewProductDialog.findViewById(R.id.products_PB);
        TextView itemsNumber=addNewProductDialog.findViewById(R.id.product_no);
        ImageView close=addNewProductDialog.findViewById(R.id.close);
        TextView add_product=addNewProductDialog.findViewById(R.id.add_product);
        extraDataRecycler=addNewProductDialog.findViewById(R.id.extra_data_recycler);
        productName=addNewProductDialog.findViewById(R.id.product_name);
        productPrice=addNewProductDialog.findViewById(R.id.product_price);
        productImage=addNewProductDialog.findViewById(R.id.productImg);
        itemsNumber.setText("");
        extraDataRecycler.setVisibility(View.VISIBLE);

        products_PB.setVisibility(View.VISIBLE);
        getProducts("1");
        productAdapter=new ProductAdapter(getApplicationContext(),null, this);
        LinearLayoutManager layoutManager =new LinearLayoutManager(getApplicationContext());
        products_recycler.setLayoutManager(layoutManager);
        products_recycler.setAdapter(productAdapter);
        products_recycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        isLoading=false;
        products_recycler.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager,2) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d("PAGINATIONN", "onLoadMore: "+isLoading);
                if (isLoading)return;
                isLoading=true;
                page++;
                if (!pages.exceedLimit(page)){
                    getProducts(String.valueOf(page));
                    productAdapter.setLoadingView(String.valueOf(page));
                }

            }
        });




        add_product.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(addNewProductDialog.getWindowToken(), 0);



            int items;
            try{
                 items=Integer.valueOf(itemsNumber.getText().toString());
            }catch (NumberFormatException ex){
                 items=0;
            }

            if (TextUtils.isEmpty(itemsNumber.getText().toString())||items<1) {
                Toast.makeText(BubbleService.this, R.string.enter_valid_quantity, Toast.LENGTH_SHORT).show();
                return;
            }

            HashMap<String,Object> values= ExtraDataHelper.getValues(getApplicationContext(),
                    extraDataAdapter,extraDataRecycler);
            if (values==null){
                Toast.makeText(this, R.string.complete_fields_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("EXTRAAAAAAA", "onClick: "+values.toString());

            if (!orderItems.contains(orderItem)){
                orderItem.setItems(items);
                orderItem.setExtras(values);
                orderItems.add(orderItem);
                cartAdapter.addCartItem(
                        new CartItem(productAdapter.getProducts()
                                .get(productAdapter.getProducts().indexOf(new ProductData(orderItem.getProduct_id()))),items));
                Toast.makeText(BubbleService.this, "product Added to order", Toast.LENGTH_SHORT).show();
                mWindowManager.removeView(addNewProductDialog);
                if (newOrderDialog!=null)newOrderDialog.setVisibility(View.VISIBLE);
                if (newOrderDialog!=null)emptyView.setVisibility(View.GONE);

            }
            else{
                Toast.makeText(BubbleService.this, "item already exist", Toast.LENGTH_SHORT).show();
            }
        });

        close.setOnClickListener(view -> {
            if (addNewProductDialog!=null) mWindowManager.removeView(addNewProductDialog);
            if (newOrderDialog!=null)newOrderDialog.setVisibility(View.VISIBLE);
        });

    }

    private void clearOrderFields() {
        if (newOrderDialog==null)
            return;

        Spinner city=newOrderDialog.findViewById(R.id.client_city);
        EditText client_name=newOrderDialog.findViewById(R.id.client_name);
        EditText client_address=newOrderDialog.findViewById(R.id.client_address);
        EditText client_area=newOrderDialog.findViewById(R.id.client_area);
        EditText client_order_phone1=newOrderDialog.findViewById(R.id.client_order_phone1);

        client_name.setText("");
        client_address.setText("");
        client_area.setText("");
        client_order_phone1.setText("");

        city.setSelection(0);
        orderItems.clear();
        cartAdapter.emptyCart();
        if (emptyView!=null)emptyView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onBlockSelected(String blockId) {
        setBroadCastLoading(View.VISIBLE);
        mWindowManager.removeView(botBlocksDialog);
        String token=SharedHelper.getKey(getApplicationContext(),LoginActivity.TOKEN);
        BaseClient.getApiService()
                .sendBroadcast(token, psid,page,blockId)
                .enqueue(new Callback<Broadcast>() {
                    @Override
                    public void onResponse(Call<Broadcast> call, Response<Broadcast> response) {
                        Broadcast broadcast=response.body();
                        if (broadcast!=null){
                            Toast.makeText(BubbleService.this, broadcast.getData(), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(BubbleService.this, "Error sending Broadcast ", Toast.LENGTH_SHORT).show();
                        }
                        setBroadCastLoading(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<Broadcast> call, Throwable t) {
                        Log.d("BROADCASTTT", "onFailure: "+t.getMessage());
                        Toast.makeText(BubbleService.this, "Error sending Broadcast", Toast.LENGTH_SHORT).show();
                        setBroadCastLoading(View.GONE);

                    }
                });
    }

    @Override
    public void onSubscriberSelected(String psid,String userId) {
        this.psid =psid;
        this.userId=userId;
        mWindowManager.removeView(subscribersDialog);
        bubbleView.findViewById(R.id.expanded_bubble).setVisibility(View.VISIBLE);
    }

    private void stopFlasher(){
        handler.removeCallbacksAndMessages(null);
        //handler.removeCallbacks(runnable);
        if (bubbleView!=null)
        bubbleView.findViewById(R.id.gotUserId)
                .setBackgroundResource(R.drawable.circle_green);
    }

    private void setBroadCastLoading(int visibility){
        if (bubbleView!=null)
        bubbleView.findViewById(R.id.broadcast_progressBar)
                .setVisibility(visibility);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bubbleView != null) mWindowManager.removeView(bubbleView);
        if (newOrderDialog != null&&newOrderDialog.getWindowToken()!=null) mWindowManager.removeView(newOrderDialog);
        if (subscribersDialog != null&&subscribersDialog.getWindowToken()!=null) mWindowManager.removeView(subscribersDialog);
        if (botBlocksDialog != null&&botBlocksDialog.getWindowToken()!=null) mWindowManager.removeView(botBlocksDialog);
        isRunning=false;
        EventBus.getDefault().unregister(this);
    }

    // method for making flasher to indicate getting user id from user name
    boolean run=false;
    private void startFlasher(){

        handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("SUBSCRIPERR","run ");
                if (run)
                    bubbleView.findViewById(R.id.gotUserId)
                            .setBackgroundResource(R.drawable.circle_green);
                else
                    bubbleView.findViewById(R.id.gotUserId)
                            .setBackgroundResource(R.drawable.circle_gray);

                handler.postDelayed(this,150);
                run=!run;
            }
        });

//        bubbleView.findViewById(R.id.gotUserId)
//                .setBackgroundResource(R.drawable.circle_green);
    }

    private boolean isAClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        return !(differenceX > CLICK_ACTION_THRESHOLD/* =5 */ || differenceY > CLICK_ACTION_THRESHOLD);
    }

    private WindowManager.LayoutParams getViewParams(int x, int y, int width, int height,WindowManager.LayoutParams layoutParams){


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            layoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    PixelFormat.TRANSLUCENT);
        }else{
            layoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    PixelFormat.TRANSLUCENT);
        }

        layoutParams.gravity = Gravity.TOP | Gravity.START;
        if (x!=-1)
        layoutParams.x = x;
        if (y!=-1)
        layoutParams.y = y;

        if (width!=-1)
            layoutParams.width=width;
        if (height!=-1)
            layoutParams.height=height;



        return layoutParams;
    }

    View.OnTouchListener onTouchListener=new View.OnTouchListener() {
        private int initialX;
        private int initialY;
        private float initialTouchX;
        private float initialTouchY;
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    initialX = params.x;
                    initialY = params.y;

                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();

                    startX = event.getX();
                    startY = event.getY();
                    return true;

                case MotionEvent.ACTION_UP:

                    float endX = event.getX();
                    float endY = event.getY();
                    if (isAClick(startX, endX, startY, endY)) {
                        if (userName==null&& psid ==null){
                            bubbleView.findViewById(R.id.gotUserName)
                                    .setBackgroundResource(R.drawable.circle_gray);
                            bubbleView.findViewById(R.id.gotUserId)
                                    .setBackgroundResource(R.drawable.circle_gray);
                            expandedView.setVisibility(View.GONE);
                            return true;
                        }
                        if (userName!=null){
                            bubbleView.findViewById(R.id.gotUserName)
                                    .setBackgroundResource(R.drawable.circle_green);
                        }
                        if (subscribers!=null&&subscribers.size()>0&&psid==null){
                            setupSubscribersDialog(subscribers);
                            Log.d("SUBSCRIBERSS", " ");
                        }

                        if (psid !=null){
                            if (expandedView.getVisibility()==View.GONE)
                                expandedView.setVisibility(View.VISIBLE);
                            else
                                expandedView.setVisibility(View.GONE);
                        }


                    }


                    return true;

                case MotionEvent.ACTION_MOVE:
                    params.x = initialX + (int) (event.getRawX() - initialTouchX);
                    params.y = initialY + (int) (event.getRawY() - initialTouchY);

                    // bubble is in the bottom and delete bubble must be shown
                    if (event.getRawY()/height>.75){
                        //Log.d("DELETEE", "is delete added "+deleteViewAdded);

                        if (!deleteViewAdded){
                            mWindowManager.addView(deleteView
                                    ,getViewParams(
                                            width/2-60
                                            ,height-50
                                            ,-1,-1,dialogParams));
                            deleteViewAdded=true;
                        }
                    }else{
                        if (deleteViewAdded){
                            mWindowManager.removeView(deleteView);
                            deleteViewAdded=false;
                        }

                    }


                    if (event.getRawY()>height-150  //100
                            &&event.getRawX()>width/2-80  //40
                            &&event.getRawX()<width/2+150  //100
                    )
                    {
                        Log.d("DELETEE", "must delete: h"+height);
                        if (deleteViewAdded){
                            if (bubbleView!=null) mWindowManager.removeView(bubbleView);
                            mWindowManager.removeView(deleteView);
                            deleteViewAdded=false;
                            bubbleView=null;
                        }
                        stopSelf();
                        isRunning=false;


                    }

                    if (bubbleView!=null)
                        mWindowManager.updateViewLayout(bubbleView, params);
                    return true;
            }
            return false;
        }
    };

    View.OnTouchListener dialogTouchListener =new View.OnTouchListener() {
        private int initialX;
        private int initialY;
        private float initialTouchX;
        private float initialTouchY;
        @Override
        public boolean onTouch(View view, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:


                    //remember the initial position.
                    initialX = dialogParams.x;
                    initialY = dialogParams.y;


                    //get the touch location
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    //Calculate the X and Y coordinates of the view.
                    dialogParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                    dialogParams.y = initialY + (int) (event.getRawY() - initialTouchY);


                    //Update the layout with new X & Y coordinate
                    mWindowManager.updateViewLayout(botBlocksDialog, dialogParams);
                    return true;
            }
            return false;
        }
    };

    View.OnTouchListener subscribersdialogTouchListener =new View.OnTouchListener() {
        private int initialX;
        private int initialY;
        private float initialTouchX;
        private float initialTouchY;
        @Override
        public boolean onTouch(View view, MotionEvent event) {

            if (subscribersDialogParams==null){
                Log.d("subscribersDialogParams", "onTouch: null subscribersDialogParams");
                return true;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:


                    //remember the initial position.
                    initialX = subscribersDialogParams.x;
                    initialY = subscribersDialogParams.y;


                    //get the touch location
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    //Calculate the X and Y coordinates of the view.
                    subscribersDialogParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                    subscribersDialogParams.y = initialY + (int) (event.getRawY() - initialTouchY);


                    //Update the layout with new X & Y coordinate
                    mWindowManager.updateViewLayout(subscribersDialog, subscribersDialogParams);
                    return true;
            }
            return false;
        }
    };

    private View.OnTouchListener setOnTOuchListener(WindowManager.LayoutParams viewparams){
        View.OnTouchListener viewTouchListener =new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if (viewparams==null){
                    Log.d("subscribersDialogParams", "onTouch: null subscribersDialogParams");
                    return true;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:


                        //remember the initial position.
                        initialX = viewparams.x;
                        initialY = viewparams.y;


                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        viewparams.x = initialX + (int) (event.getRawX() - initialTouchX);
                        viewparams.y = initialY + (int) (event.getRawY() - initialTouchY);


                        //Update the layout with new X & Y coordinate
                        if (newOrderDialog!=null)
                        mWindowManager.updateViewLayout(newOrderDialog, viewparams);
                        return true;
                }
                return false;
            }
        };

        return viewTouchListener;

    }




    private void getProducts(String page){
        String token=SharedHelper.getKey(getApplicationContext(), LoginActivity.TOKEN);
        ProductsRepo.getInstance()
                .getProducts(token,null,page,"10")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<AllProducts>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<AllProducts> response) {
                        products_PB.setVisibility(View.GONE);
                        AllProducts allProducts=response.body();
                        if (allProducts!= null&&allProducts.getProducts()!=null) {
                            pages=allProducts.getPages();
                            productAdapter.addProducts(allProducts.getProducts());

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(BubbleService.this,R.string.error_getting_products, Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void getCities2(){
        Spinner client_city=newOrderDialog.findViewById(R.id.client_city);
        if (cities != null) {
            cities.clear();
            Log.d("ORDERRRR", "clear Cities: ");
        }
        CitiesRepo
                .getInstance()
                .getCities(SharedHelper.getKey(getApplicationContext(), LoginActivity.TOKEN),
                        SharedHelper.getKey(getApplicationContext(), LoginActivity.PARENT_ID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<Cities>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<Cities> response) {
                        if (response.body()!=null&&response.body().getCities() != null) {
                            if (response.body().getCities().size() > 0) {

                                citiesBody = response.body().getCities();
                                for (Cities.City city : citiesBody) {
                                    cities.add(city.getCity_name()+" >> "+
                                            NumberFormat.getNumberInstance(Locale.getDefault()).format(
                                                    Integer.valueOf(city.getShipping_cost()))+
                                            getString(R.string.le)+" "
                                            +getString(R.string.for_shipping));
                                    citiesId.add(city.getCity_id());
                                }
                                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext()
                                        , R.layout.layout_cities_spinner_item, cities);

                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                client_city.setAdapter(adapter);
                                client_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        CITY_ID = String.valueOf(citiesBody.get(position).getCity_id());
                                        Log.d("ORDERRRR", "onItemSelected: "+CITY_ID);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }


                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }


    @Override
    public void onCartItemDeleted(int productId) {
        orderItems.remove(new OrderItem(String.valueOf(productId)));
        if (orderItems!=null&&orderItems.isEmpty()){
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCartItemQuantityIncrease(int productId,int quantity) {
        updateQuantity(productId,quantity);
    }

    @Override
    public void onCartItemQuantityDecrease(int productId,int quantity) {
        updateQuantity(productId,quantity);

    }

    private void updateQuantity(int productId,int quantity){
        orderItems.get(
                orderItems.indexOf(
                        new OrderItem(String.valueOf(productId))))
                .setItems(quantity);

    }

    @Override
    public void onProductClicked(ProductData product) {
        //selectedProduct=product;
        orderItem.setProduct_id(product.getProduct_id());

        productName.setText(product.getProduct_name());
        Picasso.get()
                .load(product.getProduct_image())
                .into(productImage);
        productPrice.setText(String.valueOf(product.getProduct_real_price()));


        extraDataAdapter=new ExtraDataAdapter2(getApplicationContext()
                ,product.getExtra_data());
        extraDataRecycler.setAdapter(extraDataAdapter);
        extraDataRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}
