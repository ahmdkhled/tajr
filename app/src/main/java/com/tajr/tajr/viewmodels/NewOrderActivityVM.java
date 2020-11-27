package com.tajr.tajr.viewmodels;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.crashlytics.android.Crashlytics;
import com.tajr.tajr.R;
import com.tajr.tajr.models.MainResponse;
import com.tajr.tajr.models.UserTimePayload;
import com.tajr.tajr.repository.WorkTimeRepo;

import java.util.concurrent.TimeUnit;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class NewOrderActivityVM extends AndroidViewModel {

    private MutableLiveData<MainResponse> sendUserTime=new MutableLiveData<>();
    private MutableLiveData<Boolean> isUserTimeSending=new MutableLiveData<>();
    private MutableLiveData<String> userTimeSendingError=new MutableLiveData<>();
    private long startTime=-1;

    public NewOrderActivityVM(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<MainResponse> SendUserTime(UserTimePayload userTimePayload) {

        sendUserTime=new MutableLiveData<>();
        isUserTimeSending.setValue(true);
        WorkTimeRepo.getInstance()
                .setUserTime(userTimePayload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<MainResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("userWorkTime", "onSubscribe: ");
                    }

                    @Override
                    public void onSuccess(Response<MainResponse> response) {
                        Log.d("userWorkTime", "onSuccess: ");

                        MainResponse mainResponse=response.body();
                        if (response.isSuccessful()&&mainResponse!=null){

                            sendUserTime.setValue(mainResponse);

                        }
                        else{
                            userTimeSendingError.setValue(Resources.getSystem().getString(R.string.server_error));
                            Crashlytics.logException(new Throwable("Error parsing Work Time Response"));

                        }
                        isUserTimeSending.setValue(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("userWorkTime",e.getMessage());
                        userTimeSendingError.setValue(Resources.getSystem().getString(R.string.server_error));
                        isUserTimeSending.setValue(false);
                        Crashlytics.logException(e);

                    }
                });
        return sendUserTime;
    }

    public MutableLiveData<Boolean> getIsWorkTimeSending() {
        return WorkTimeRepo.getInstance().getIsWorkTimeSending();
    }

    public MutableLiveData<String> getWorkTimeSendingError() {
        return WorkTimeRepo.getInstance().getWorkTimeSendingError();
    }

    public long getStartTime() {
        return startTime==-1? startTime=TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()):startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
