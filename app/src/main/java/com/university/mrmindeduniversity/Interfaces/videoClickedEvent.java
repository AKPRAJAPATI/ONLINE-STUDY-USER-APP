package com.university.mrmindeduniversity.Interfaces;

import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.university.mrmindeduniversity.player.playerActivity;

public interface videoClickedEvent {
    void onItemClicked(String uniqueKey, String videoUrl, String videoN , TextView videoName, ConstraintLayout linearLayout, Class<playerActivity> gotoNextActivity);
    void checkItem(String uniqueKey, TextView videoName, ConstraintLayout constraintLayout);
}
