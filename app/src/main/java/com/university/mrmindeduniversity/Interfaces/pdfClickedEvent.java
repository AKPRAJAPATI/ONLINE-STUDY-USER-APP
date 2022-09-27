package com.university.mrmindeduniversity.Interfaces;

import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.university.mrmindeduniversity.Application.ShowPdfActivity;

public interface pdfClickedEvent {
    void onItemClicked(String uniqueKey, String pdfUrl, TextView pdfName, ConstraintLayout linearLayout, Class<ShowPdfActivity> gotoNextActivity);
    void checkItem(String uniqueKey, TextView pdfName, ConstraintLayout linearLayout);
}
