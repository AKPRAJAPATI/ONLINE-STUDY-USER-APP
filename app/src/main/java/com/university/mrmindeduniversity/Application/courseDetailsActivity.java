package com.university.mrmindeduniversity.Application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.university.mrmindeduniversity.Adapters.pdfAdapters;
import com.university.mrmindeduniversity.Interfaces.pdfClickedEvent;
import com.university.mrmindeduniversity.MainActivity;
import com.university.mrmindeduniversity.Models.pdfModel;
import com.university.mrmindeduniversity.R;
import com.university.mrmindeduniversity.databinding.ActivityCourseDetailsBinding;
import com.university.mrmindeduniversity.player.videoListActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class courseDetailsActivity extends AppCompatActivity implements pdfClickedEvent {
    private ActivityCourseDetailsBinding binding;
    private pdfAdapters adapters;
    private ArrayList<pdfModel> course_arrayList;
    private String thumbnail, thumbnailKey;
    private String adminKey;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//////////////////////////////////// GET DATA /////////////////////////////////////////////////\
        thumbnail = getIntent().getStringExtra("thumbnail");
        thumbnailKey = getIntent().getStringExtra("uniqueKey");
//////////////////////////////////// GET DATA /////////////////////////////////////////////////


        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        sharedPreferences = getSharedPreferences("mrMinded", MODE_PRIVATE);
        adminKey = sharedPreferences.getString("key", "");

        Picasso.get().load(thumbnail).into(binding.courseThumbnail);
        binding.pdfListRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.pdfListRecyclerview.setHasFixedSize(true);
        course_arrayList = new ArrayList<>();
        getOurPdfs();
        adapters = new pdfAdapters(getApplicationContext(), course_arrayList, this);
        binding.pdfListRecyclerview.setAdapter(adapters);

        binding.playCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), videoListActivity.class);
                intent.putExtra("adminKey", adminKey);
                intent.putExtra("thumbnail", thumbnail);
                intent.putExtra("thumbnailkey", thumbnailKey);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    private void getOurPdfs() {
        databaseReference.child("Admin").child(adminKey).child("Course").child(adminKey).child(thumbnailKey).child("pdf")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            course_arrayList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                pdfModel model = dataSnapshot.getValue(pdfModel.class);
                                model.setPdfUniqueKey(dataSnapshot.getKey());
                                course_arrayList.add(model);
                                binding.coursenotfound.setVisibility(View.GONE);
                            }
                            adapters.notifyDataSetChanged();
                        } else {
                            binding.coursenotfound.setVisibility(View.VISIBLE);
                            course_arrayList.clear();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onItemClicked(String uniqueKey, String pdfUrl, TextView pdfName, ConstraintLayout background, Class<ShowPdfActivity> gotoNextActivity) {
        databaseReference.child("Admin").child(adminKey).child("Course").child(adminKey).child(thumbnailKey).child("pdf").child(uniqueKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                  //  boolean checkClickItem = snapshot.child("clicked").getValue(boolean.class);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("clicked",true);
                    databaseReference.child("Admin").child(adminKey).child("Course").child(adminKey).child(thumbnailKey).child("pdf").child(uniqueKey).updateChildren(hashMap);

                 Intent intent = new Intent(getApplicationContext(), gotoNextActivity.asSubclass(ShowPdfActivity.class));
                 intent.putExtra("pdfUrl",pdfUrl);
                 intent.putExtra("thumbnail",thumbnail);
                 intent.putExtra("uniqueKey",thumbnailKey);
                 intent.putExtra("pdfUniqueKey",uniqueKey);

                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                 startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void checkItem(String uniqueKey, TextView pdfName, ConstraintLayout linearLayout) {
        databaseReference.child("Admin").child(adminKey).child("Course").child(adminKey).child(thumbnailKey).child("pdf").child(uniqueKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    boolean checkClickItem = snapshot.child("clicked").getValue(boolean.class);
                    if (checkClickItem == true) {
                        linearLayout.setBackgroundColor(getResources().getColor(R.color.black_60));
                        pdfName.setTextColor(getResources().getColor(R.color.red));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}