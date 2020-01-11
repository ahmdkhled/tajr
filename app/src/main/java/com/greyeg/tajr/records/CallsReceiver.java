package com.greyeg.tajr.records;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.greyeg.tajr.activities.EmptyCallActivity;
import com.greyeg.tajr.activities.LoginActivity;
import com.greyeg.tajr.helper.CallTimeManager;
import com.greyeg.tajr.helper.CurrentCallListener;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.models.CallTimePayload;
import com.greyeg.tajr.models.CallTimeResponse;
import com.greyeg.tajr.models.SimpleOrderResponse;
import com.greyeg.tajr.order.CurrentOrderData;
import com.greyeg.tajr.order.NewOrderActivity;
import com.greyeg.tajr.order.models.CurrentOrderResponse;
import com.greyeg.tajr.over.MissedCallNoOrderService;
import com.greyeg.tajr.over.MissedCallOrderService;
import com.greyeg.tajr.repository.CallTimeRepo;
import com.greyeg.tajr.server.Api;
import com.greyeg.tajr.server.BaseClient;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by VS00481543 on 25-10-2017.
 */

public class CallsReceiver extends BroadcastReceiver {

    public static String phoneNumber;
    public static String name;
    public static CurrentCallListener currentCallListener;
    public static boolean inOrderActivity = false;
    public static boolean inCall = false;
    static boolean recordStarted;
    private static String savedNumber;
    private static int outgoing=-1;

    public static void setCurrentCallListener(CurrentCallListener listener) {
        currentCallListener = listener;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        String state=intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String number=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

        String orderPhone=null;
        if (CurrentOrderData.getInstance().getCurrentOrderResponse()!=null)
             orderPhone =CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getPhone1();

        //Log.d("CALLLLLLL", "number : "+number );
        Log.d("CALLLLLLL", "state : "+state );
        Log.d("CALLLLLLL", "action : "+intent.getAction() );
        if (number==null){
            if (intent.getAction()!=null&&intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL))
                outgoing=1;
            Log.d("CALLLLLLL", "inside outgoing : "+outgoing);

            return;
        }

        Log.d("CALLLLLLL", "outgoing : "+outgoing);
        Log.d("CALLLLLLL", "------------------------ : ");


        if (state!=null&& state.equals(TelephonyManager.EXTRA_STATE_IDLE)&&outgoing==1){
            getCallDuration(context,number,orderPhone);
        }

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
//        Toast.makeText(context, "تم بدء مكالمة", Toast.LENGTH_SHORT).show();
        boolean switchCheckOn = pref.getBoolean("switchOn", true);
        if (switchCheckOn) try {


            if (intent.getAction()!=null&&intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
                final Intent intent2 = new Intent(context, EmptyCallActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        context.startActivity(intent2);
                    }
                }, 2000);

            } else {
            }

            System.out.println("Receiver Start");

            Bundle extras = intent.getExtras();



            if (state!=null&&state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

                String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Toast.makeText(context, "incoming call  " + incomingNumber, Toast.LENGTH_SHORT).show();
                Api api = BaseClient.getBaseClient().create(Api.class);
                api.getPhoneData2(
                        SharedHelper.getKey(context, LoginActivity.TOKEN),
                        SharedHelper.getKey(context, LoginActivity.USER_ID),
                        incomingNumber
                ).enqueue(new Callback<CurrentOrderResponse>() {
                    @Override
                    public void onResponse(Call<CurrentOrderResponse> call, Response<CurrentOrderResponse> response) {
                        Log.d("eslamfaisalmissedcall", response.body().getCode()+" onResponse: "+response.toString());
//                        if (response.body().getCode().equals("1401")) {
//                            Intent startHoverIntent = new Intent(context, MissedCallNoOrderService.class);
//                            context.startService(startHoverIntent);
//                        } else
//
                        if (response.body().getCode().equals("1200")) {
                            CurrentOrderData.getInstance().setMissedCallOrderResponse(response.body());
                            MissedCallOrderService.showFloatingMenu(context);
                        }

                    }

                    @Override
                    public void onFailure(Call<CurrentOrderResponse> call, Throwable t) {

                    }
                });

                /*int j=pref.getInt("numOfCalls",0);
                pref.edit().putInt("numOfCalls",++j).apply();
                Log.d(TAG, "onReceive: num of calls "+ pref.getInt("numOfCalls",0));*/
            } else if (state!=null&&state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)/*&& pref.getInt("numOfCalls",1)==1*/) {

                int j = pref.getInt("numOfCalls", 0);
                pref.edit().putInt("numOfCalls", ++j).apply();

                if (CurrentOrderData.getInstance().getCurrentOrderResponse()!=null)
                phoneNumber = CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getPhone1();

                if (pref.getInt("numOfCalls", 1) == 1) {
                    Intent reivToServ = new Intent(context, RecorderService.class);
                    reivToServ.putExtra("number", phoneNumber);
                    context.startService(reivToServ);
                    inCall = true;
                    //name=new CommonMethods().getContactName(phoneNumber,context);
                }

            } else if (state!=null&&state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

                int k = pref.getInt("numOfCalls", 1);
                pref.edit().putInt("numOfCalls", --k).apply();
                int l = pref.getInt("numOfCalls", 0);
                recordStarted = pref.getBoolean("recordStarted", false);
                context.stopService(new Intent(context, RecorderService.class));
                inCall = false;

                pref.edit().putBoolean("recordStarted", false).apply();
                int serialNumber = pref.getInt("serialNumData", 1);

                //recordStarted=true;
                pref.edit().putInt("serialNumData", ++serialNumber).apply();
                pref.edit().putBoolean("recordStarted", true).apply();


                if (currentCallListener != null) {
                    currentCallListener.callEnded(serialNumber, phoneNumber);
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
            //todo cache call
            Log.d("CALLLLLLL", e.getMessage());

        }
    }


    private void getCallDuration(Context context,String number,String orderPhone){
        if (!number.equals(orderPhone))return;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Cursor c = context.getContentResolver().query(

                        CallLog.Calls.CONTENT_URI,

                        null, null, null,

                        CallLog.Calls.DATE + " DESC "+ " LIMIT 1");

                while (c!=null&&c.moveToNext()){
                    Log.d("CALLLLLLLL", "getCallDuration: "
                            +"  "+c.getString(c.getColumnIndex(CallLog.Calls.NUMBER))
                            +"   "+c.getString(c.getColumnIndex(CallLog.Calls.DURATION))
                            +"   "+c.getString(c.getColumnIndex(CallLog.Calls.DATE))

                    );
                    String duration=c.getString(c.getColumnIndex(CallLog.Calls.DURATION));
                    String date=c.getString(c.getColumnIndex(CallLog.Calls.DURATION));
                    CallTimeManager.getInstance(context)
                            .saveCallSession(duration,date);
                }
                if (c!=null)
                    c.close();


            }
        },200);




    }




}
