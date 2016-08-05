package com.android.kirannote;

import android.app.Application;

import com.android.kirannote.injection.component.ApplicationComponent;
import com.android.kirannote.injection.component.DaggerApplicationComponent;
import com.android.kirannote.injection.module.ApplicationModule;

/**
 * Created by kiran.kumar on 8/3/16.
 */
public class NoteApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        buildInjection();
        new AppInitializer().appInitProcess(applicationComponent);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        applicationComponent = null;
    }

    private void buildInjection(){
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent(){
        return applicationComponent;
    }

    private ApplicationComponent applicationComponent;
}
