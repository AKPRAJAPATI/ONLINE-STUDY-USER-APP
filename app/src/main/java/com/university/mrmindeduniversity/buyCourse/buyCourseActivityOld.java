package com.university.mrmindeduniversity.buyCourse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.university.mrmindeduniversity.Adapters.buyCourseAdapters;
import com.university.mrmindeduniversity.MainActivity;
import com.university.mrmindeduniversity.Models.courseModel;
import com.university.mrmindeduniversity.databinding.ActivityBuyCourseBinding;

import java.util.ArrayList;

public class buyCourseActivityOld extends AppCompatActivity {
    private ActivityBuyCourseBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private buyCourseAdapters adapters;
    private ArrayList<courseModel> courseModelArrayList;

    private String adminKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuyCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        SharedPreferences sharedPreferences = getSharedPreferences("mrMinded", MODE_PRIVATE);
        adminKey = sharedPreferences.getString("key", "");

        binding.recyclerViewMain.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewMain.setHasFixedSize(true);
        courseModelArrayList = new ArrayList<>();
        getOurCourse();
        adapters = new buyCourseAdapters(getApplicationContext(), courseModelArrayList);
        binding.recyclerViewMain.setAdapter(adapters);

    }
    private void getOurCourse() {
        databaseReference.child("Admin").child(adminKey).child("Course").child(adminKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    courseModelArrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        courseModel model = dataSnapshot.getValue(courseModel.class);
                        model.setUniquekey(dataSnapshot.getKey());
                        courseModelArrayList.add(model);
                    }
                    adapters.notifyDataSetChanged();
                } else {
                    courseModelArrayList.clear();
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
        startActivity(new Intent(buyCourseActivityOld.this, MainActivity.class));
    }
}