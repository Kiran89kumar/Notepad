package com.android.kirannote.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.kirannote.NoteApplication;
import com.android.kirannote.R;
import com.android.kirannote.entities.db.NoteData;
import com.android.kirannote.injection.component.ApplicationComponent;
import com.android.kirannote.injection.component.DaggerListComponent;
import com.android.kirannote.injection.module.ActivityModule;
import com.android.kirannote.ui.adapter.ItemClickListener;
import com.android.kirannote.ui.adapter.ListAdapter;
import com.android.kirannote.ui.mvp.presenter.ListPresenter;
import com.android.kirannote.ui.mvp.view.NotesListView;
import com.android.kirannote.ui.view.DividerDecoration;
import com.android.kirannote.utils.Constant;

import java.util.List;

import javax.inject.Inject;

/**
 * ListActivity: Shows the list of notes in list format
 */
public class ListActivity extends AppCompatActivity implements ItemClickListener<NoteData>,NotesListView{

    private static final String TAG = ListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initView();
        initDI();
    }

    /**
     * initView: Used to initialize the View of Activity
     */
    private void initView(){
        contentView = (CoordinatorLayout) findViewById(R.id.contentPanel);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        noteList = (RecyclerView) findViewById(R.id.notelist);
        progressBarView = (RelativeLayout) findViewById(R.id.rl_progress);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(toolbar);
    }

    //Initializing the dependency injection and then setting the Recyclerview adapter
    private void initDI(){
        ApplicationComponent component = ((NoteApplication)getApplication()).getApplicationComponent();
        DaggerListComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(component)
                .build().inject(this);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(noteList.getContext());
        noteList.setLayoutManager(linearLayoutManager);

        if(noteList.getAdapter() != adapter)
            noteList.setAdapter(adapter);

        noteList.addItemDecoration(new DividerDecoration(noteList.getContext(), LinearLayoutManager.VERTICAL));
    }

    private void initEvents(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpBottomSheet();
            }
        });
        adapter.setItemClickListener(this);
    }

    /**
     * Setting the Bottom sheet for Inserting the new Note.
     */
    private void setUpBottomSheet(){
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetView = getLayoutInflater().inflate(R.layout.view_bottomsheet, null);
        //init bottom sheet views

        final EditText edtTitle = (EditText) bottomSheetView.findViewById(R.id.edt_Title);
        final EditText edtDesc = (EditText) bottomSheetView.findViewById(R.id.edt_desc);
        final Button btnsave = (Button) bottomSheetView.findViewById(R.id.btn_save);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottomSheetDialog!=null && bottomSheetDialog.isShowing()){
                    String title = edtTitle.getText().toString();
                    String desc = edtDesc.getText().toString();
                    if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc)){
                        NoteData item = new NoteData();
                        item.setHeading(title);
                        item.setDescription(desc);
                        item.setRemoved(0);
                        presenter.saveNote(item);
                        bottomSheetDialog.dismiss();
                    }else{
                        Snackbar.make(contentView,"Blank data not allowed",Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                btnsave.setOnClickListener(null);
                bottomSheetDialog = null;
            }
        });
    }

    @Override
    public void onClick(NoteData item, int position, View view) {
        Intent detailActivity = new Intent(ListActivity.this, DetailActivity.class);
        detailActivity.putExtra(Constant.ITEM_ID, item.getId());
        detailActivity.putExtra(Constant.ITEM_TITLE, item.getHeading());
        startActivity(detailActivity);
    }

    @Override
    public void onDeleteClicked(int position, NoteData item) {
        presenter.deleteNote(item.getId());
    }

    @Override
    public void renderNoteList(List<NoteData> notesList) {
        adapter.addItem(notesList);
    }

    /**
     * Reloading the List.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onstart");
        initEvents();
        this.presenter.setView(this);
        if(noteList!=null && noteList.getAdapter()!=null){
            Log.i(TAG,"starting loading");
            presenter.loadNotesList();
        }
    }

    /**
     * Destroying all the view and presenter. To Avoid Memory leak
     */
    @Override
    protected void onStop() {
        super.onStop();
        presenter.destroy();
        fab.setOnClickListener(null);
        adapter.setItemClickListener(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showLoading() {
        progressBarView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBarView.setVisibility(View.GONE);
    }

    @Override
    public void showError(String errorMsg) {
        Snackbar.make(contentView, errorMsg, Snackbar.LENGTH_LONG).show();
    }

    @Inject
    ListAdapter adapter;

    @Inject
    ListPresenter presenter;

    CoordinatorLayout contentView;
    Toolbar toolbar;
    RecyclerView noteList;
    RelativeLayout progressBarView;
    FloatingActionButton fab;
    BottomSheetDialog bottomSheetDialog;
    View bottomSheetView;
}
