package com.tajr.tajr.viewmodels;

import android.app.Application;
import android.text.PrecomputedText;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthResult;
import com.tajr.tajr.models.RefreshTokenRes;
import com.tajr.tajr.models.Response;
import com.tajr.tajr.repository.GsignInRepo;
import com.tajr.tajr.repository.RemoteConfigRepo;

public class MainActivityVm extends AndroidViewModel {

    private MutableLiveData<Boolean> isLatestVersion;

    public MainActivityVm(@NonNull Application application) {
        super(application);
        application.getApplicationContext();
    }

    public MutableLiveData<Boolean> getIsLatestVersion() {

        return isLatestVersion==null?isLatestVersion=RemoteConfigRepo.getInstance()
                .getRemoteConfigValue(getApplication().getApplicationContext()):isLatestVersion;
    }

    public MutableLiveData<String> getError() {
        return RemoteConfigRepo.getInstance().getError();
    }


    //----------------------

    public MutableLiveData<AuthResult> signIn(String idToken){
        return  GsignInRepo.signIn(idToken);
    }

    public  MutableLiveData<Response<RefreshTokenRes>> getRefreshToken( String code, String client_id,String client_secret) {
        return  GsignInRepo.getRefreshToken(code,client_id,client_secret);

    }
    }
