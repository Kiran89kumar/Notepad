package com.android.kirannote.ui.mvp.view;

/**
 * Created by kiran.kumar on 8/4/16.
 */
public interface IView {

    void showLoading();

    void hideLoading();

    void showError(String errorMsg);
}
