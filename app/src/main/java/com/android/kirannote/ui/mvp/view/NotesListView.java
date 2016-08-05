package com.android.kirannote.ui.mvp.view;

import com.android.kirannote.entities.db.NoteData;

import java.util.List;

/**
 * Created by kiran.kumar on 8/4/16.
 */
public interface NotesListView extends IView {
    void renderNoteList(List<NoteData> notesList);
}
