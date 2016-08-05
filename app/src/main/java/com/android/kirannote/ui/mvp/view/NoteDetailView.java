package com.android.kirannote.ui.mvp.view;

import com.android.kirannote.entities.db.NoteData;

/**
 * Created by kiran.kumar on 8/4/16.
 */
public interface NoteDetailView extends IView {
    void renderNote(NoteData noteData);
    int getId();
}
