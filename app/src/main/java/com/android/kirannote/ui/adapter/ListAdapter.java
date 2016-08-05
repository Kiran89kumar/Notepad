package com.android.kirannote.ui.adapter;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.kirannote.R;
import com.android.kirannote.entities.db.NoteData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by kiran.kumar on 8/4/16.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemViewHolder> {

    @Inject
    public ListAdapter(){
        this.noteDatas = new ArrayList<>();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater minInflater = LayoutInflater.from(parent.getContext());
        ViewGroup viewGroup = (ViewGroup) minInflater.inflate(R.layout.item_note, parent, false);
        return new ItemHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        NoteData item = noteDatas.get(position);
        holder.setData(item, position);
    }

    @Override
    public int getItemCount() {
        return noteDatas.size();
    }

    public void addItem(List<NoteData> list){
        noteDatas.clear();
        noteDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener<NoteData> listener){
        this.listener = listener;
    }

    abstract class ItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, ViewAdapterHolder<NoteData> {

        public ItemViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener == null)
                return;
            int position = getLayoutPosition();
            NoteData item = (NoteData) getItem(position);
            View imgView = v.findViewById(R.id.txt_headline);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                imgView.setTransitionName(item.getId() + "");

            if (listener != null)
                listener.onClick(item, position, imgView);
        }

    }

    class ItemHolder extends ItemViewHolder {

        public ItemHolder(View view) {
            super(view);
            txtHeading = (TextView) view.findViewById(R.id.txt_headline);
            txtDesc = (TextView) view.findViewById(R.id.txt_desc);
            imgBtnDelete = (ImageButton) view.findViewById(R.id.imgbtn_delete);
            imgBtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getLayoutPosition();
                        NoteData item = (NoteData) getItem(position);
                        listener.onDeleteClicked(position, item);
                    }
                }
            });
        }

        @Override
        public void setData(NoteData data, int position) {
            txtHeading.setText(data.getHeading());
            txtDesc.setText(data.getDescription());
        }

        TextView txtHeading, txtDesc;
        ImageButton imgBtnDelete;
    }

    protected Object getItem(int position) {
        if (position < noteDatas.size()) {
            return noteDatas.get(position);
        } else {
            return null;
        }
    }

    private List<NoteData> noteDatas;
    private ItemClickListener<NoteData> listener;

}
