package com.university.mrmindeduniversity.player;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.university.mrmindeduniversity.R;
import com.university.mrmindeduniversity.databinding.ActivityPlayerBinding;


public class playerActivity extends AppCompatActivity {
    private ActivityPlayerBinding binding;

    String live_url;
    String videoName;
    String adminKey;
    String thumbnail;
    String thumbnailkey;
    String uniquekey_get;
    String videoKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        live_url = getIntent().getStringExtra("videoUrl");
        videoName = getIntent().getStringExtra("name");
        adminKey = getIntent().getStringExtra("adminKey");
        thumbnail = getIntent().getStringExtra("thumbnail");
        thumbnailkey = getIntent().getStringExtra("thumbnailkey");
        videoKey = getIntent().getStringExtra("videoKey");

        binding.videoName.setText(videoName);
        binding.fullscreenVideoView.videoUrl(live_url).
                addSeekBackwardButton().
                addSeekForwardButton().
                enableAutoStart().
                playDrawable(bg.devlabs.fullscreenvideoview.R.drawable.ic_play_arrow_white_48dp).
                pauseDrawable(bg.devlabs.fullscreenvideoview.R.drawable.ic_pause_white_48dp).
                fastForwardDrawable(R.drawable.ic_baseline_fast_forward_24).
                fastForwardSeconds(10).
                rewindDrawable(R.drawable.ic_baseline_fast_rewind_24).
                rewindSeconds(10)
                .enterFullscreenDrawable(bg.devlabs.fullscreenvideoview.R.drawable.ic_fullscreen_white_48dp)
                .exitFullscreenDrawable(bg.devlabs.fullscreenvideoview.R.drawable.ic_fullscreen_white_48dp);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), videoListActivity.class);

        intent.putExtra("adminKey", adminKey);
        intent.putExtra("thumbnailkey", thumbnailkey);
        intent.putExtra("thumbnail", thumbnail);
        intent.putExtra("videoKey", videoKey);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}