package com.university.mrmindeduniversity.Adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.university.mrmindeduniversity.Application.ShowPdfActivity;
import com.university.mrmindeduniversity.Interfaces.pdfClickedEvent;
import com.university.mrmindeduniversity.Models.pdfModel;
import com.university.mrmindeduniversity.R;
import com.university.mrmindeduniversity.databinding.PdfListItemBinding;

import java.util.ArrayList;

public class pdfAdapters extends RecyclerView.Adapter<pdfAdapters.mainViewHolder> {


    private Context context;
    private ArrayList<pdfModel> arrayList;
    private pdfClickedEvent pdfClickedEven;


    private SharedPreferences sharedPreferences;
    private String adminKey;


    public pdfAdapters(Context context, ArrayList<pdfModel> arrayList, pdfClickedEvent pdfClickedEven) {
        this.context = context;
        this.arrayList = arrayList;
        this.pdfClickedEven = pdfClickedEven;
    }

    @NonNull
    @Override
    public mainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new mainViewHolder(LayoutInflater.from(context).inflate(R.layout.pdf_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull mainViewHolder holder, int position) {
        pdfModel model = arrayList.get(position);
        holder.binding.pdfName.setText(model.getPdfName());

        sharedPreferences = context.getSharedPreferences("mrMinded", MODE_PRIVATE);
        adminKey = sharedPreferences.getString("key", "");

        pdfClickedEven.checkItem(model.getPdfUniqueKey(),holder.binding.pdfName,holder.binding.back);

        holder.binding.pdfName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfClickedEven.onItemClicked(model.getPdfUniqueKey(),model.getPdfUrl(),holder.binding.pdfName,holder.binding.back, ShowPdfActivity.class);
            }
        });
        holder.binding.contentNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfClickedEven.onItemClicked(model.getPdfUniqueKey(),model.getPdfUrl(),holder.binding.pdfName,holder.binding.back, ShowPdfActivity.class);
            }
        });
        holder.binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfClickedEven.onItemClicked(model.getPdfUniqueKey(),model.getPdfUrl(),holder.binding.pdfName,holder.binding.back, ShowPdfActivity.class);
            }
        });

        //////////////marque work ////////////////////

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class mainViewHolder extends RecyclerView.ViewHolder {
        private PdfListItemBinding binding;

        public mainViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = PdfListItemBinding.bind(itemView);
        }
    }
}
