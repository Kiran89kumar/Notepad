package com.android.kirannote.ui.mvp.presenter;

import android.util.Log;

import com.android.kirannote.entities.db.NoteData;
import com.android.kirannote.entities.db.NoteDataLoader;
import com.android.kirannote.ui.mvp.view.NoteDetailView;
import com.android.kirannote.utils.RxScheduler;
import com.android.kirannote.utils.RxUtils;

import javax.inject.Inject;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kiran.kumar on 8/4/16.
 */
public class DetailPresenter implements IPresenter {

    @Inject
    DetailPresenter(){
    }

    public void setView(NoteDetailView view){
        this.view = view;
        subscriptions = RxUtils.getNewCompositeSubscription(subscriptions);
    }

    public void loadNote(){
        Log.i(TAG,"Load Notes Called"+view.getId());
        view.showLoading();
        this.getNotesList(view.getId());
    }

    public void editNote(NoteData item) {
        Log.i(TAG,"Load Notes Called"+view.getId());
        view.showLoading();
        this.updateNote(item);
    }

    private void getNotesList(int id){
        subscriptions.add(dbLoader.getObservableNote(id)
                            .compose(scheduler.<NoteData>ioViewable())
                            .subscribe(new Subscriber<NoteData>() {
                                @Override
                                public void onCompleted() {
                                    Log.i(TAG, "Getting Notes Completed");
                                    view.hideLoading();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(TAG, "Getting Notes Error:"+e.getMessage());
                                    view.hideLoading();
                                }

                                @Override
                                public void onNext(NoteData noteData) {
                                    Log.d(TAG, "Getting Notes onNext");
                                    view.renderNote(noteData);
                                }
                            }));
    }

    private void updateNote(NoteData data){
        subscriptions.add(dbLoader.getObservableUpdateNote(data)
                .compose(scheduler.<NoteData>ioViewable())
                .subscribe(new Subscriber<NoteData>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "Getting Notes Completed");
                        view.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Getting Notes Error:"+e.getMessage());
                        view.hideLoading();
                    }

                    @Override
                    public void onNext(NoteData noteData) {
                        Log.d(TAG, "Getting Notes onNext");
                        view.renderNote(noteData);
                    }
                }));
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        RxUtils.unsubscribeIfNotNull(subscriptions);
        this.view = null;
    }

    @Inject
    NoteDataLoader dbLoader;

    @Inject
    RxScheduler scheduler;

    private NoteDetailView view;
    private CompositeSubscription subscriptions = new CompositeSubscription();
    private final static String TAG = DetailPresenter.class.getSimpleName();


}
