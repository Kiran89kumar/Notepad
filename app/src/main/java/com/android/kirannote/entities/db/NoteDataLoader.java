package com.android.kirannote.entities.db;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by kiran.kumar on 8/4/16.
 */
public class NoteDataLoader {

    @Inject
    public NoteDataLoader(DBHelper dbHelper){
        this.appDB = dbHelper;
    }

    public Observable<List<NoteData>> getObservableListItems(){
        return Observable.create(new Observable.OnSubscribe<List<NoteData>>() {
            @Override
            public void call(Subscriber<? super List<NoteData>> subscriber) {
                try {
                    noteDataList = loadItems();
                } catch (SQLException ex) {
                    subscriber.onError(ex);
                    return;
                }
                subscriber.onNext(noteDataList);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<NoteData> getObservableNote(final int notesID){
        return Observable.create(new Observable.OnSubscribe<NoteData>() {
            @Override
            public void call(Subscriber<? super NoteData> subscriber) {
                try {
                    noteData = getNoteData(notesID);
                } catch (SQLException ex) {
                    subscriber.onError(ex);
                    return;
                }
                subscriber.onNext(noteData);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<NoteData> getObservableUpdateNote(final NoteData data){
        return Observable.create(new Observable.OnSubscribe<NoteData>() {
            @Override
            public void call(Subscriber<? super NoteData> subscriber) {
                try {
                    noteData = updateNote(data);
                } catch (SQLException ex) {
                    subscriber.onError(ex);
                    return;
                }
                subscriber.onNext(noteData);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<List<NoteData>> getObservableListItemsAfterSave(final NoteData data){
        return Observable.create(new Observable.OnSubscribe<List<NoteData>>() {
            @Override
            public void call(Subscriber<? super List<NoteData>> subscriber) {
                try {
                    noteDataList = saveNote(data);
                } catch (SQLException ex) {
                    subscriber.onError(ex);
                    return;
                }
                subscriber.onNext(noteDataList);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<List<NoteData>> getObservableDeleteItem(final int id){
        return Observable.create(new Observable.OnSubscribe<List<NoteData>>() {
            @Override
            public void call(Subscriber<? super List<NoteData>> subscriber) {
                try {
                    noteDataList = deleteNote(id);
                } catch (SQLException ex) {
                    subscriber.onError(ex);
                    return;
                }
                subscriber.onNext(noteDataList);
                subscriber.onCompleted();
            }
        });
    }

    private List<NoteData> loadItems()
            throws SQLException {
        NoteDataDAO dataDAO = null;
        try {
            dataDAO = new NoteDataDAO(appDB);
            dataDAO.open();

            return dataDAO.getNoteData();

        } finally {
            if (null != dataDAO) {
                dataDAO.close();
            }
        }
    }

    private NoteData getNoteData(int notesId)
            throws SQLException {
        NoteDataDAO dataDAO = null;
        try {
            dataDAO = new NoteDataDAO(appDB);
            dataDAO.open();

            return dataDAO.getNoteData(notesId);

        } finally {
            if (null != dataDAO) {
                dataDAO.close();
            }
        }
    }

    private List<NoteData> saveNote(NoteData data)
            throws SQLException {
        NoteDataDAO dataDAO = null;
        try {
            dataDAO = new NoteDataDAO(appDB);
            dataDAO.open();
            return dataDAO.insertNotes(data);

        } finally {
            if (null != dataDAO) {
                dataDAO.close();
            }
        }
    }

    private NoteData updateNote(NoteData data)
            throws SQLException {
        NoteDataDAO dataDAO = null;
        try {
            dataDAO = new NoteDataDAO(appDB);
            dataDAO.open();

            return dataDAO.updateNote(data);

        } finally {
            if (null != dataDAO) {
                dataDAO.close();
            }
        }
    }

    private List<NoteData> deleteNote(int id)
            throws SQLException {
        NoteDataDAO dataDAO = null;
        try {
            dataDAO = new NoteDataDAO(appDB);
            dataDAO.open();

            return dataDAO.deleteNote(id);

        } finally {
            if (null != dataDAO) {
                dataDAO.close();
            }
        }
    }

    private final DBHelper appDB;
    private List<NoteData> noteDataList;
    private NoteData noteData;
}
