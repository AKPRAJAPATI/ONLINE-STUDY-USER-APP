package com.university.mrmindeduniversity.player;

import android.content.Intent;
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
import com.university.mrmindeduniversity.Adapters.videoListAdapters;
import com.university.mrmindeduniversity.Interfaces.videoClickedEvent;
import com.university.mrmindeduniversity.MainActivity;
import com.university.mrmindeduniversity.Models.videoListModel;
import com.university.mrmindeduniversity.R;
import com.university.mrmindeduniversity.databinding.ActivityVideoListBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class videoListActivity extends AppCompatActivity implements videoClickedEvent {
    private ActivityVideoListBinding binding;
    private videoListAdapters adapters;
    private ArrayList<videoListModel> course_arrayList;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String thumbnailkey, adminkey, thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ///////////////////////////////////////////////// GET DATA ////////////////////////////////////////
        adminkey = getIntent().getStringExtra("adminKey");
        thumbnailkey = getIntent().getStringExtra("thumbnailkey");
        thumbnail = getIntent().getStringExtra("thumbnail");
        ///////////////////////////////////////////////// GET DATA ////////////////////////////////////////



        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        binding.pdfListRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.pdfListRecyclerview.setHasFixedSize(true);
        course_arrayList = new ArrayList<>();
        getOurAllVideoes();
        adapters = new videoListAdapters(getApplicationContext(), course_arrayList, this);
        binding.pdfListRecyclerview.setAdapter(adapters);



    }

    private void getOurAllVideoes() {
        databaseReference.child("Admin").child(adminkey).child("Course").child(adminkey).child(thumbnailkey).child("videos")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            course_arrayList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                videoListModel model = dataSnapshot.getValue(videoListModel.class);
                                model.setVideoUniqueKey(dataSnapshot.getKey());
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
    public void onItemClicked(String uniqueKey, String pdfUrl, String videoN, TextView pdfName, ConstraintLayout background, Class<playerActivity> gotoNextActivity) {
        databaseReference.child("Admin").child(adminkey).child("Course").child(adminkey).child(thumbnailkey).child("videos").child(uniqueKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("clicked",true);
                    databaseReference.child("Admin").child(adminkey).child("Course").child(adminkey).child(thumbnailkey).child("videos").child(uniqueKey).updateChildren(hashMap);
                    Intent intent = new Intent(getApplicationContext(), gotoNextActivity.asSubclass(playerActivity.class));
                    intent.putExtra("videoUrl",pdfUrl);

                    intent.putExtra("name",videoN);
                    intent.putExtra("videoKey", uniqueKey);

                    intent.putExtra("adminKey", adminkey);
                    intent.putExtra("thumbnailkey", thumbnailkey);
                    intent.putExtra("thumbnail", thumbnail);
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
        databaseReference.child("Admin").child(adminkey).child("Course").child(adminkey).child(thumbnailkey).child("videos").child(uniqueKey).addValueEventListener(new ValueEventListener() {
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
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("thumbnail", thumbnail);
        intent.putExtra("thumbnailkey", thumbnailkey);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}