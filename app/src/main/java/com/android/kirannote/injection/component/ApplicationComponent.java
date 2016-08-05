package com.android.kirannote.injection.component;

import com.android.kirannote.AppInitializer;
import com.android.kirannote.NoteApplication;
import com.android.kirannote.entities.db.DBHelper;
import com.android.kirannote.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by kiran.kumar on 8/3/16.
 */

@Singleton @Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    NoteApplication application();
    DBHelper dbHelper();
    void inject(AppInitializer initializer);
}
