package com.university.mrmindeduniversity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.university.mrmindeduniversity.Adapters.mainAdapters;
import com.university.mrmindeduniversity.Models.courseModel;
import com.university.mrmindeduniversity.authentication.LoginActivity;
import com.university.mrmindeduniversity.authentication.RegisterActivity;
import com.university.mrmindeduniversity.buyCourse.buyCourseActivityOld;
import com.university.mrmindeduniversity.databinding.ActivityMainBinding;
import com.university.mrmindeduniversity.settings.adminActivity;
import com.university.mrmindeduniversity.Adapters.mainAdapters;
import com.university.mrmindeduniversity.Models.courseModel;
import com.university.mrmindeduniversity.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private List<SlideModel> arrayList;
    private String adminKey;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private ShimmerFrameLayout shimmerFrameLayout;
    private mainAdapters adapters;
    private ArrayList<courseModel> courseModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        binding.userNameLogo.setSelected(true);

        arrayList = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("mrMinded", MODE_PRIVATE);
        adminKey = sharedPreferences.getString("key", "");


        //////////////////////////////////GET ALL TYPE NOTIFICATION///////////////////////
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        //////////////////////////////////GET ALL TYPE NOTIFICATION///////////////////////
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), adminActivity.class));
            }
        }); // this is for goto settings admin share email whatsapp etc.
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), buyCourseActivityOld.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        //////////////////////////////////////////////// RECYCLER VIEW//////////////////////////////////////
        binding.recyclerViewMain.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewMain.setHasFixedSize(true);
        courseModelArrayList = new ArrayList<>();
        getOurCourse();
        adapters = new mainAdapters(getApplicationContext(), courseModelArrayList);
        binding.recyclerViewMain.setAdapter(adapters);
        //////////////////////////////////////////////// RECYCLER VIEW//////////////////////////////////////

        /////////////////////////////methods/////////////////////////////
        getOurSliders(arrayList);
        getOurSliders(arrayList);
        binding.imageSlider.setImageList(arrayList);
        /////////////////////////////methods/////////////////////////////
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    private void getOurSliders(List<SlideModel> arrayList) {
        databaseReference.child("Admin").child(adminKey).child("Sliders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String image_data = dataSnapshot.child("slidersImg").getValue(String.class);
                    arrayList.add(new SlideModel(image_data, ScaleTypes.CENTER_CROP));
                    binding.imageSlider.setImageList(arrayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
}