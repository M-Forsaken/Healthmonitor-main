package com.app.healthmonitor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewItemModel extends ViewModel {
    private final MutableLiveData<String> appState = new MutableLiveData<String>();
    public void setAppState(String state) {
        appState.setValue(state);
    }
    public LiveData<String> getAppState() {
        return appState;
    }

}
