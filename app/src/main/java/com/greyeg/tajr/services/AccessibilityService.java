package com.greyeg.tajr.services;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.lifecycle.LifecycleObserver;

import com.greyeg.tajr.activities.MainActivity;
import com.greyeg.tajr.R;
import com.greyeg.tajr.activities.LoginActivity;
import com.greyeg.tajr.activities.SettingsActivity;
import com.greyeg.tajr.helper.NetworkUtil;
import com.greyeg.tajr.helper.SessionManager;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.helper.TimeCalculator;
import com.greyeg.tajr.helper.UserNameEvent;
import com.greyeg.tajr.models.Activity;
import com.greyeg.tajr.models.MainResponse;
import com.greyeg.tajr.models.UserTimePayload;
import com.greyeg.tajr.repository.WorkTimeRepo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class AccessibilityService extends android.accessibilityservice.AccessibilityService implements LifecycleObserver {

    public static final String TAG="ACCESSIBLILTYYY";
    private static String lastOpenedApp="";
    private static long startTime=-1;
    Toast toast;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

        if (!SharedHelper.getBooleanValue(getApplicationContext(), SettingsActivity.BUBBLE_SETTING))return;

        if (accessibilityEvent!=null&&
                accessibilityEvent.getPackageName()!=null&&
                accessibilityEvent.getPackageName().equals("com.facebook.pages.app"))
        {


            if (accessibilityEvent.getEventType()==AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){

                if (NetworkUtil.getConnectivityStatus(getApplicationContext())==NetworkUtil.TYPE_NOT_CONNECTED){

                    if (toast.getView()==null||!toast.getView().isShown()){
                        toast.show();
                    }
                    TimeCalculator.getInstance(getApplicationContext()).stopTimer();
                    return;
                }
                if (startTime==-1) {
                    startTime= TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                }
                TimeCalculator.getInstance(getApplicationContext()).startTimer(new Activity(-1,startTime,"PM"));
                Log.d("TIMERCALCCzz", "timer start "+startTime);



            checkOverlayPermission();
            String userName=getUserName();
            if (userName!=null)
            EventBus.getDefault().post(new UserNameEvent(userName));
        }
        }else {
            Log.d("TTIMERCALCCCC",accessibilityEvent!=null
                    ?"\n"+lastOpenedApp+"\n"
                    +"\n"+accessibilityEvent.getClassName()+"\n"
                    +"\n"+accessibilityEvent.getPackageName()+"\n"
                    +"----------------------------------- \n"

                    :"nulllll");
            if (lastOpenedApp.equals("com.facebook.pages.app")
                    &&accessibilityEvent!=null
                    &&!accessibilityEvent.getPackageName().equals("com.touchtype.swiftkey")
                    &&!(accessibilityEvent.getClassName()!=null&&
                    ( accessibilityEvent.getClassName().equals("android.view.ViewGroup")||
                            accessibilityEvent.getClassName().equals("android.widget.LinearLayout")    )
                    &&accessibilityEvent.getPackageName().equals("com.greyeg.tajr"))  ){

                Log.d("TIMERCALCCCCx", "timer stop ");
                //Toast.makeText(this, "timer stop", Toast.LENGTH_SHORT).show();

                TimeCalculator.getInstance(getApplicationContext()).stopTimer();
                sendWorkTime();


            }


        }


        if (accessibilityEvent!=null&&accessibilityEvent.getPackageName()!=null
        &&!(accessibilityEvent.getPackageName().equals("com.greyeg.tajr")
                &&( accessibilityEvent.getClassName().equals("android.view.ViewGroup")||
                accessibilityEvent.getClassName().equals("android.widget.LinearLayout")    )))
        lastOpenedApp=accessibilityEvent.getPackageName().toString();
        Log.d("TIMERCALCCCC",">>>>>>>>> "+lastOpenedApp);
        }

    void sendWorkTime(){

        if (!NetworkUtil.isConnected(this)){
            TimeCalculator.getInstance(this).setStatus(TimeCalculator.PM,TimeCalculator.FAILED_TO_UPLOAD);
            return;

        }

        TimeCalculator.getInstance(this).setStatus(TimeCalculator.PM,TimeCalculator.UPLOADING);
        String token= SessionManager.getInstance(getApplicationContext()).getToken();
        ArrayList<Activity> activityList=TimeCalculator.getInstance(getApplicationContext()).getIdleWorkTime();
        UserTimePayload userTimePayload=
                new UserTimePayload(token,activityList);

        WorkTimeRepo.getInstance()
                .setUserTime(userTimePayload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<MainResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<MainResponse> response) {
                        MainResponse mainResponse=response.body();
                        if (response.isSuccessful()&&mainResponse!=null){
                            TimeCalculator.getInstance(getApplicationContext()).clear(activityList);
                            //Log.d("userWorkTime", "time after end: " + value +" time stamp");
                            for (Activity activity:TimeCalculator.getInstance(getApplicationContext()).getWorkTimeActivity()){
                                Log.d("TIMERCALCCzz", "send activity : "+activity);
                            }
                            startTime=-1;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        TimeCalculator.getInstance(getApplicationContext()).setStatus(TimeCalculator.PM,TimeCalculator.FAILED_TO_UPLOAD);

                    }
                });

    }

    @Override
    public void onInterrupt() {

    }


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        toast=Toast.makeText(this, R.string.no_connection_message, Toast.LENGTH_SHORT);
        Log.d(TAG, "onServiceConnected: ");
    }

    private void checkOverlayPermission(){
        Log.d(TAG, "checkOverlayPermission: ");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M&&!Settings.canDrawOverlays(getApplicationContext())){
                Intent intent=new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else {
                Log.d(TAG, "show bubble from accessibility:" );
                showBubble();
            }



    }

    void showBubble(){
        if (!BubbleService.isRunning)
        startService(new Intent(this, BubbleService.class));
    }


    private String getUserName(){
        AccessibilityNodeInfo root =getRootInActiveWindow();


        if (root!=null&&root.getChildCount()>1 &&root.getChild(1)!=null
        &&root.getChild(0).getClassName().equals("android.widget.ImageView")
        &&root.getChild(1).getClassName().equals("android.view.ViewGroup")
        ) {
            AccessibilityNodeInfo curent = root
                    .getChild(1);
            for (int i = 0; i < curent.getChildCount(); i++) {

                AccessibilityNodeInfo username=curent.getChild(0);
                if (username.getText()!=null)
                    return username.getText().toString();
            }

            Log.d(TAG, "///////////////////////////////////");
        }else{
            Log.d(TAG, "getUserName: not found");
        }
       return null;
    }


}
