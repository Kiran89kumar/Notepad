package com.android.kirannote.injection.component;

import android.content.Context;

import com.android.kirannote.injection.PerActivity;
import com.android.kirannote.injection.module.ActivityModule;

import dagger.Component;

/**
 * Created by kiran.kumar on 8/4/16.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface ActivityComponent {
    Context context();
}
