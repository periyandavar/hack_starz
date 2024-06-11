package com.vicky.hackstraz;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class AboutUsViewModel1 {
    private MutableLiveData<String> mText;

    public AboutUsViewModel1() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
