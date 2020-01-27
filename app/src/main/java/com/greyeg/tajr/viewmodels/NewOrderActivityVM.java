package com.greyeg.tajr.viewmodels;

import android.content.res.Resources;

import androidx.lifecycle.MutableLiveData;

import com.crashlytics.android.Crashlytics;
import com.greyeg.tajr.R;
import com.greyeg.tajr.models.MainResponse;
import com.greyeg.tajr.models.UserTimePayload;
import com.greyeg.tajr.models.UserWorkTimeResponse;
import com.greyeg.tajr.repository.WorkTimeRepo;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class NewOrderActivityVM {

    private MutableLiveData<MainResponse> sendUserTime;
    private MutableLiveData<Boolean> isUserTimeSending;
    private MutableLiveData<String> userTimeSendingError;

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

                    }

                    @Override
                    public void onSuccess(Response<MainResponse> response) {
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
}
