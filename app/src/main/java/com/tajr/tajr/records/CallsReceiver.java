package com.tajr.tajr.records;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.tajr.tajr.activities.EmptyCallActivity;
import com.tajr.tajr.helper.CallTimeManager;
import com.tajr.tajr.helper.CurrentCallListener;
import com.tajr.tajr.helper.SessionManager;
import com.tajr.tajr.models.CurrentOrderData;
import com.tajr.tajr.models.CurrentOrderResponse;
import com.tajr.tajr.view.over.MissedCallOrderService;
import com.tajr.tajr.repository.PhoneRepo;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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
        Log.d("CALLLLLLL", "onReceive: ");
        String state=intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String number=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        String action=intent.getAction();

        String orderPhone=null;
        if (CurrentOrderData.getInstance().getCurrentOrderResponse()!=null)
             orderPhone =CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getPhone1();

        Log.d("CALLLLLLL", "state : "+state );
        Log.d("CALLLLLLL", "action : "+intent.getAction() );
        Log.d("CALLLLLLL", "number : "+intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER) );

         if (action!=null&&action.equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            Log.d("CALLLLLLL", "outgoing call ya man: ");
             getCallDuration(context,number,orderPhone);


        }else {
             Log.d("CALLLLLLL", "incoming call ya man call ya man: ");
             if (state!=null&&state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {


                 String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                 Log.d("CALLLLLLL","incoming number "+incomingNumber);
                 if (incomingNumber==null)return;
                         //Toast.makeText(context, "incoming call  " + incomingNumber, Toast.LENGTH_SHORT).show();
//                 Api api = BaseClient.getBaseClient().create(Api.class);
//                 api.getPhoneData2(
//                         SharedHelper.getKey(context, LoginActivity.TOKEN),
//                         SharedHelper.getKey(context, LoginActivity.USER_ID),
//                         incomingNumber
//                 ).enqueue(new Callback<CurrentOrderResponse>() {
//                     @Override
//                     public void onResponse(Call<CurrentOrderResponse> call, Response<CurrentOrderResponse> response) {
//                         if (response.body()!=null&&response.body().getCode().equals("1200")) {
//                             CurrentOrderData.getInstance().setMissedCallOrderResponse(response.body());
//                             MissedCallOrderService.showFloatingMenu(context);
//                         }
//
//                     }
//
//                     @Override
//                     public void onFailure(Call<CurrentOrderResponse> call, Throwable t) {
//                         Log.d("eslamfaisalmissedcall", t.getMessage());
//                     }
//                 });

                 PhoneRepo.getInstance()
                         .getPhoneData(SessionManager.getInstance(context).getToken(),
                                 SessionManager.getInstance(context).getUserId(),
                                 incomingNumber)
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new SingleObserver<Response<CurrentOrderResponse>>() {
                             @Override
                             public void onSubscribe(Disposable d) {

                             }

                             @Override
                             public void onSuccess(Response<CurrentOrderResponse> response) {
                                 CurrentOrderResponse currentOrderResponse=response.body();
                                 if (response.isSuccessful()&&currentOrderResponse!=null
                                 &&currentOrderResponse.getCode().equals("1200")){
                                     CurrentOrderData.getInstance().setMissedCallOrderResponse(response.body());
                                     MissedCallOrderService.showFloatingMenu(context);
                                 }
                             }

                             @Override
                             public void onError(Throwable e) {
                                //todo handle error
                             }
                         });


             }

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

            }
            System.out.println("Receiver Start");

        } catch (Exception e) {
            e.printStackTrace();
            //todo cache call
            Log.d("CALLLLLLL", e.getMessage());
        }
        Log.d("CALLLLLLL", "________________________________________________: ");
    }


    private void getCallDuration(Context context,String number,String orderPhone){
        if (number==null||!number.equals(orderPhone))return;
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
