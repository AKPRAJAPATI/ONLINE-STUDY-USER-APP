package com.university.mrmindeduniversity.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.university.mrmindeduniversity.R;


public class adminActivity extends AppCompatActivity {
    ImageView imageEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        imageEmail = findViewById(R.id.imageView2);
        imageEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = "iammrminded@gmail.com";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + mail));
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(intent);
            }
        });
    }

    private void openTelegram() {
        String url = "https://t.me/+KCwF_2wkUjQ5ZTk9";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void yout(View view) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/c/MrMindedOfficial"));
        startActivity(appIntent);
    }

    public void whatsapp(View view) {
        String phoneNumberWithCountryCode = "+917481938597";
        String message = "Hello, How are you? ";

        startActivity(
                new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                String.format("https://api.whatsapp.com/send?phone=%s&text=%s", phoneNumberWithCountryCode, message)
                        )
                )
        );

    }

    public void telegram(View view) {
        openTelegram();
    }

}
