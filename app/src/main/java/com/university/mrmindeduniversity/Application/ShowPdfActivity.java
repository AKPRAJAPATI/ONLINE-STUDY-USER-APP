package com.university.mrmindeduniversity.Application;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.university.mrmindeduniversity.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ShowPdfActivity extends AppCompatActivity {

    private String pdfUrl;
    private String pdfUniqueKey;
    private PDFView pdfView;
    private String thumbnail;
    private String thumbnailKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf);

        pdfUrl = getIntent().getStringExtra("pdfUrl");
        pdfUniqueKey = getIntent().getStringExtra("pdfUniqueKey");
        thumbnail = getIntent().getStringExtra("thumbnail");
        thumbnailKey = getIntent().getStringExtra("uniqueKey");

        pdfView = findViewById(R.id.idPDFView);
        new RetrivePDFfromUrl().execute(pdfUrl);

    }
    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),courseDetailsActivity.class);
        intent.putExtra("thumbnail",thumbnail);
        intent.putExtra("uniqueKey",thumbnailKey);
        intent.putExtra("pdfUniqueKey",pdfUniqueKey);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}