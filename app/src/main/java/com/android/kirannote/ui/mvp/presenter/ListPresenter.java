package com.android.kirannote.ui.mvp.presenter;

import android.util.Log;

import com.android.kirannote.entities.db.NoteData;
import com.android.kirannote.entities.db.NoteDataLoader;
import com.android.kirannote.ui.mvp.view.NotesListView;
import com.android.kirannote.utils.RxScheduler;
import com.android.kirannote.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by kiran.kumar on 8/4/16.
 */
public class ListPresenter implements IPresenter {

    @Inject
    ListPresenter(){
    }

    public void setView(NotesListView view){
        this.view = view;
        subscriptions = RxUtils.getNewCompositeSubscription(subscriptions);
    }

    public void loadNotesList(){
        Log.i(TAG,"Load Notes Called");
        view.showLoading();
        this.getNotesList();
    }

    public void saveNote(NoteData item) {
        Log.i(TAG,"Save Note Called");
        view.showLoading();
        this.saveNoteToDB(item);
    }

    public void deleteNote(int id) {
        Log.i(TAG,"Save Note Called");
        view.showLoading();
        this.deleteFromDB(id);
    }

    private void getNotesList(){
        subscriptions.add(dbLoader.getObservableListItems()
                            .compose(scheduler.<List<NoteData>>ioViewable())
                            .subscribe(new NoteScheduler()));
    }

    private void saveNoteToDB(NoteData data){
        subscriptions.add(dbLoader.getObservableListItemsAfterSave(data)
                            .compose(scheduler.<List<NoteData>>ioViewable())
                            .subscribe(new NoteScheduler()));
    }

    private void deleteFromDB(int id){
        subscriptions.add(dbLoader.getObservableDeleteItem(id)
                            .compose(scheduler.<List<NoteData>>ioViewable())
                            .subscribe(new NoteScheduler()));
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

    private final class NoteScheduler extends rx.Subscriber<List<NoteData>>{
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
        public void onNext(List<NoteData> list) {
            Log.d(TAG, "Getting Notes onNext"+list.size());
            view.renderNoteList(list);
        }
    }

    @Inject
    NoteDataLoader dbLoader;

    @Inject
    RxScheduler scheduler;

    private NotesListView view;
    private CompositeSubscription subscriptions = new CompositeSubscription();
    private final static String TAG = ListPresenter.class.getSimpleName();


}
