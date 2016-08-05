package com.android.kirannote.ui.adapter;

import android.view.View;

/**
 * Created by kiran.kumar on 8/5/16.
 */
public interface ItemClickListener<T> {
    void onClick(T item, int position, View view);

    void onDeleteClicked(int position, T item);
}
