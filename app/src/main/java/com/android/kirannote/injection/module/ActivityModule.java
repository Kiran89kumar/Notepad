package com.android.kirannote.injection.module;

import android.content.Context;

import com.android.kirannote.injection.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
  private final Context mContext;

  public ActivityModule(Context mContext) {
    this.mContext = mContext;
  }

  @Provides @PerActivity
  Context provideActivityContext() {
    return mContext;
  }
}
