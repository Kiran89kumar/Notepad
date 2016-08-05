package com.android.kirannote.injection.component;

import android.content.Context;

import com.android.kirannote.injection.PerActivity;
import com.android.kirannote.injection.module.ActivityModule;
import com.android.kirannote.ui.activity.DetailActivity;
import com.android.kirannote.ui.activity.ListActivity;

import dagger.Component;

/**
 * Created by kiran.kumar on 8/4/16.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface ListComponent extends ActivityComponent{
    void inject(ListActivity activity);
    void inject(DetailActivity activity);
    Context activityContext();
}
