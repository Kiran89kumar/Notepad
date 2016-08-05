package com.android.kirannote.injection.module;

import com.android.kirannote.NoteApplication;
import com.android.kirannote.entities.db.DBHelper;
import com.android.kirannote.utils.RxScheduler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kiran.kumar on 8/3/16.
 */
@Module
public class ApplicationModule {

    public ApplicationModule(NoteApplication noteApplication){
        this.noteApplication = noteApplication;
    }

    @Provides @Singleton
    NoteApplication providesApplication(){
        return noteApplication;
    }

    @Provides @Singleton
    RxScheduler providesRxScheduler(){
        return new RxScheduler();
    }

    @Provides
    @Singleton
    DBHelper provideDatabaseHelper(NoteApplication context) {
        return new DBHelper(context);
    }

    private final NoteApplication noteApplication;
}
