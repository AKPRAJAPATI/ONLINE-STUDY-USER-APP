package com.university.mrmindeduniversity.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.university.mrmindeduniversity.Interfaces.videoClickedEvent;
import com.university.mrmindeduniversity.Models.videoListModel;
import com.university.mrmindeduniversity.R;
import com.university.mrmindeduniversity.databinding.CourseListRowItemsBinding;
import com.university.mrmindeduniversity.player.playerActivity;

import java.util.ArrayList;

public class videoListAdapters extends RecyclerView.Adapter<videoListAdapters.mainViewHolder> {

    private Context context;
    private ArrayList<videoListModel> arrayList;
    private videoClickedEvent clickedEvent;

    public videoListAdapters(Context context, ArrayList<videoListModel> arrayList, videoClickedEvent clickedEvent) {
        this.context = context;
        this.arrayList = arrayList;
        this.clickedEvent = clickedEvent;
    }

    @NonNull
    @Override
    public mainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new mainViewHolder(LayoutInflater.from(context).inflate(R.layout.course_list_row_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull mainViewHolder holder, int position) {
        videoListModel model = arrayList.get(position);
        holder.binding.contentTitle.setText(model.getVideoName());
        clickedEvent.checkItem(model.getVideoUniqueKey(),holder.binding.contentTitle,holder.binding.constraint);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedEvent.onItemClicked(model.getVideoUniqueKey(),model.getVideoUrl(), model.getVideoName() ,holder.binding.contentTitle,holder.binding.constraint, playerActivity.class);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class mainViewHolder extends RecyclerView.ViewHolder {
        private CourseListRowItemsBinding binding;

        public mainViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CourseListRowItemsBinding.bind(itemView);
        }
    }
}
