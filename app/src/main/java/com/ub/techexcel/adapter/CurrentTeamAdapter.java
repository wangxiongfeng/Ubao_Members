package com.ub.techexcel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ubao.techexcel.R;
import com.ubao.techexcel.info.Favorite;

import java.util.List;

/**
 * Created by wang on 2018/2/8.
 */

public class CurrentTeamAdapter extends RecyclerView.Adapter<CurrentTeamAdapter.RecycleHolder> {
    private List<String> list;
    private Context context;

    public CurrentTeamAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public interface OnItemLectureListener {
        void onItem(Favorite lesson);

        void onenterItem();
    }

    public void setOnItemLectureListener(OnItemLectureListener onItemLectureListener) {
        this.onItemLectureListener = onItemLectureListener;
    }

    private OnItemLectureListener onItemLectureListener;

    @Override
    public RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.currentteam_item, parent, false);
        RecycleHolder holder = new RecycleHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, int position) {
        final String item = list.get(position);
        holder.documetname.setText(item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder {
        TextView documetname;

        public RecycleHolder(View itemView) {
            super(itemView);
            documetname = (TextView) itemView.findViewById(R.id.documetname);
        }
    }

}


