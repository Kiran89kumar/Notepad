package com.android.kirannote.utils;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kiran.kumar on 8/4/16.
 */
public abstract class AbstractRxScheduler {
    @StringDef({IO, COMPUTATION, ANDROID_MAIN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Method {}

    public Scheduler scheduler(@Method String schedule) {
        switch (schedule.toLowerCase()) {
            case IO :
                return Schedulers.io();
            case COMPUTATION :
                return Schedulers.computation();
            case ANDROID_MAIN :
                return AndroidSchedulers.mainThread();
            default :
                return Schedulers.immediate();
        }
    }

    public  <T> Observable.Transformer<T, T> ioViewable() {
        return schedulers(IO, ANDROID_MAIN);
    }

    public  <T> Observable.Transformer<T, T> computeViewable() {
        return schedulers(COMPUTATION, ANDROID_MAIN);
    }

    public <T> Observable.Transformer<T, T> fullIO() {
        return schedulers(IO, IO);
    }

    public  <T> Observable.Transformer<T, T> fullCompute() {
        return schedulers(COMPUTATION, COMPUTATION);
    }


    private <T> Observable.Transformer<T, T> schedulers(final @Method String subscribeOn,
                                                        final @Method String observeOn) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .observeOn(find(observeOn))
                        .subscribeOn(find(subscribeOn));
            }
        };
    }

    protected abstract Scheduler find(@Method String method);


    public static final String IO = "io";
    public static final String COMPUTATION = "computation";
    public static final String ANDROID_MAIN = "android_main";
}
