package com.android.kirannote.utils;

import javax.inject.Inject;

import rx.Scheduler;

/**
 * Created by kiran.kumar on 8/4/16.
 */
public class RxScheduler extends AbstractRxScheduler{

    @Inject
    public RxScheduler(){

    }

    @Override
    protected Scheduler find(@Method String method) {
        return scheduler(method);
    }
}
