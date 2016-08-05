package com.android.kirannote.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.kirannote.NoteApplication;
import com.android.kirannote.R;
import com.android.kirannote.entities.db.NoteData;
import com.android.kirannote.injection.component.ApplicationComponent;
import com.android.kirannote.injection.component.DaggerListComponent;
import com.android.kirannote.injection.module.ActivityModule;
import com.android.kirannote.ui.mvp.presenter.DetailPresenter;
import com.android.kirannote.ui.mvp.view.NoteDetailView;
import com.android.kirannote.utils.Constant;

import javax.inject.Inject;


/**
 * Activity to show the Detail of the notes.
 */
public class DetailActivity extends AppCompatActivity implements NoteDetailView{

    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString(Constant.ITEM_TITLE);
        id = bundle.getInt(Constant.ITEM_ID);
        initView();
        initDI();
    }

    //Initializing the views
    private void initView(){
        contentView = (CoordinatorLayout) findViewById(R.id.contentPanel);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textDetail = (TextView) findViewById(R.id.txt_detail);
        toolbar.setTitle(title);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(toolbar);
    }

    //Initializing the dependency injection
    private void initDI(){
        ApplicationComponent component = ((NoteApplication)getApplication()).getApplicationComponent();
        DaggerListComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(component)
                .build().inject(this);
    }

    private void initEvents(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpBottomSheet();
            }
        });
    }

    //BottomSheet for Editing the Content of Note.
    private void setUpBottomSheet(){
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetView = getLayoutInflater().inflate(R.layout.view_bottomsheet_edit, null);
        //init bottom sheet views
        final EditText edtTitle = (EditText) bottomSheetView.findViewById(R.id.edt_Title);
        final EditText edtDesc = (EditText) bottomSheetView.findViewById(R.id.edt_desc);

        if(data!=null){
            edtTitle.setText(data.getHeading());
            edtDesc.setText(data.getDescription());
        }

        final Button btnEdit = (Button) bottomSheetView.findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottomSheetDialog!=null && bottomSheetDialog.isShowing()){
                    String title = edtTitle.getText().toString();
                    String desc = edtDesc.getText().toString();

                    String originalTitle = data.getHeading();
                    String originalDesc = data.getDescription();

                    if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc)){
                        if(!title.equalsIgnoreCase(originalTitle) || !desc.equalsIgnoreCase(originalDesc)){
                            data.setDescription(desc);
                            data.setHeading(title);
                            presenter.editNote(data);
                        }else {
                            Snackbar.make(contentView,"Original content not changed.",Snackbar.LENGTH_SHORT).show();
                        }
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
                btnEdit.setOnClickListener(null);
                bottomSheetDialog = null;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initEvents();
        this.presenter.setView(this);
        if(TextUtils.isEmpty(textDetail.getText())){
            Log.i(TAG,"starting loading");
            presenter.loadNote();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.presenter.destroy();
        fab.setOnClickListener(null);
        toolbar.setNavigationOnClickListener(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String errorMsg) {

    }

    @Override
    public void renderNote(NoteData noteData) {
        data = noteData;
        toolbar.setTitle(noteData.getHeading());
        textDetail.setText(noteData.getDescription());
    }

    @Override
    public int getId() {
        return id;
    }

    @Inject
    DetailPresenter presenter;

    CoordinatorLayout contentView;
    Toolbar toolbar;
    TextView textDetail;
    FloatingActionButton fab;

    BottomSheetDialog bottomSheetDialog;
    View bottomSheetView;

    private NoteData data;
    private String title;
    private int id;

}
