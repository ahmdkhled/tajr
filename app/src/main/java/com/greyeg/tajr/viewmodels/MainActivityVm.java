package com.greyeg.tajr.viewmodels;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.greyeg.tajr.repository.RemoteConfigRepo;

public class MainActivityVm extends ViewModel {

    private MutableLiveData<Boolean> isLatestVersion;

    public MutableLiveData<Boolean> getIsLatestVersion() {
        return isLatestVersion==null?isLatestVersion=RemoteConfigRepo.getInstance().getRemoteConfigValue():isLatestVersion;
    }

    public MutableLiveData<String> getError() {
        return RemoteConfigRepo.getInstance().getError();
    }
}
