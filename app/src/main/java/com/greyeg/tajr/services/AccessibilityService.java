package com.greyeg.tajr.services;

import android.content.Intent;
import android.net.Network;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.Observer;
import com.greyeg.tajr.MainActivity;
import com.greyeg.tajr.R;
import com.greyeg.tajr.activities.LoginActivity;
import com.greyeg.tajr.activities.SettingsActivity;
import com.greyeg.tajr.helper.NetworkUtil;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.helper.TimeCalculator;
import com.greyeg.tajr.helper.UserNameEvent;
import com.greyeg.tajr.models.UserWorkTimeResponse;
import com.greyeg.tajr.repository.WorkTimeRepo;
import com.greyeg.tajr.viewmodels.NewOrderActivityVM;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class AccessibilityService extends android.accessibilityservice.AccessibilityService implements LifecycleObserver {

    public static final String TAG="ACCESSIBLILTYYY";
    private static String lastOpenedApp="";
    Toast toast;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

        if (accessibilityEvent!=null&&
                accessibilityEvent.getPackageName()!=null&&
                accessibilityEvent.getPackageName().equals("com.facebook.pages.app"))
        {


            if (accessibilityEvent.getEventType()==AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){

                if (NetworkUtil.getConnectivityStatus(getApplicationContext())==NetworkUtil.TYPE_NOT_CONNECTED){

                    if (toast.getView()==null||!toast.getView().isShown()){
                        toast.show();
                    }
                    //TimeCalculator.getInstance(getApplicationContext()).stopTimer();
                    return;
                }
                Log.d("TIMERCALCC", "timer start ");
                //TimeCalculator.getInstance(getApplicationContext()).startTimer();
                Log.d(TAG, "ttttttttttt: "+SharedHelper.getBooleanValue(getApplicationContext(), SettingsActivity.BUBBLE_SETTING));

                if (!SharedHelper.getBooleanValue(getApplicationContext(), SettingsActivity.BUBBLE_SETTING))return;

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

                Log.d("TIMERCALCCCC", "timer stop ");
                //Toast.makeText(this, "timer stop", Toast.LENGTH_SHORT).show();

                //TimeCalculator.getInstance(getApplicationContext()).stopTimer();
                if (NetworkUtil.getConnectivityStatus(getApplicationContext())!=NetworkUtil.TYPE_NOT_CONNECTED){
                    //sendWorkTime(TimeCalculator.getInstance(getApplicationContext()).getWorkTime());
                }

            }


        }


        if (accessibilityEvent!=null&&accessibilityEvent.getPackageName()!=null
        &&!(accessibilityEvent.getPackageName().equals("com.greyeg.tajr")
                &&( accessibilityEvent.getClassName().equals("android.view.ViewGroup")||
                accessibilityEvent.getClassName().equals("android.widget.LinearLayout")    )))
        lastOpenedApp=accessibilityEvent.getPackageName().toString();
        Log.d("TIMERCALCCCC",">>>>>>>>> "+lastOpenedApp);
        }

    void sendWorkTime(long activity){

        String token= SharedHelper.getKey(getApplicationContext(), LoginActivity.TOKEN);
        WorkTimeRepo.getInstance()
                .sendWorkTime2(token,String.valueOf(activity),null,"PM")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<UserWorkTimeResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("WORKKTIMMEE", "onSubscribe: ");
                    }

                    @Override
                    public void onSuccess(Response<UserWorkTimeResponse> userWorkTimeResponse) {
                        Log.d("WORKKTIMMEE", "onSuccess: "+userWorkTimeResponse.body().getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("WORKKTIMMEE", "onError: "+e.getMessage());
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
                Log.d(TAG, curent.getChild(i).getClassName()
                        + " " + curent.getText() + "  "
                        + " " + curent.getChildCount());


                Log.d(TAG, "child : "+curent.getChild(0).getText());
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
