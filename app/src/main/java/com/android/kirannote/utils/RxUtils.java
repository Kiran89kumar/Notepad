package com.android.kirannote.utils;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kiran.kumar on 8/4/16.
 */
public class RxUtils {
    public static void unsubscribeIfNotNull(Subscription subscription){
        if(subscription !=null){
            subscription.unsubscribe();
        }
    }

    public static CompositeSubscription getNewCompositeSubscription(CompositeSubscription subscription){
        if(subscription ==null || subscription.isUnsubscribed()){
            return new CompositeSubscription();
        }

        return subscription;
    }

    public static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            T observed = func.call();
                            if (observed != null) { // to make defaultIfEmpty work
                                subscriber.onNext(observed);
                            }
                            subscriber.onCompleted();
                        } catch (Exception ex) {
                            subscriber.onError(ex);
                        }
                    }
                });
    }
}
