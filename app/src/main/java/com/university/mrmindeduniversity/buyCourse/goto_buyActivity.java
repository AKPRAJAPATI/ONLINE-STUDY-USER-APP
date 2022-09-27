package com.university.mrmindeduniversity.buyCourse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.university.mrmindeduniversity.Application.courseDetailsActivity;
import com.university.mrmindeduniversity.MainActivity;
import com.university.mrmindeduniversity.databinding.ActivityGotoBuyBinding;

public class goto_buyActivity extends AppCompatActivity {
    private ActivityGotoBuyBinding binding;

    private DatabaseReference databaseReference;
    private String coursename;
    private int price;
    private String courseThumbnail;
    private String uniqueKey;
    private String adminKey;
    private boolean launchedOrNot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGotoBuyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("mrMinded", MODE_PRIVATE);
        adminKey = sharedPreferences.getString("key", "");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        coursename = getIntent().getStringExtra("course_name");
        price = getIntent().getIntExtra("course_price", 0);

        String pc = String.valueOf(price);
        courseThumbnail = getIntent().getStringExtra("thumbnail");
        uniqueKey = getIntent().getStringExtra("uniqueKey");

        binding.coursePricGoto.setText("Course Price :- " + pc);
        binding.courseNameGoto.setText("Course Name :- " + coursename);
        Picasso.get().load(courseThumbnail).into(binding.courseThumbnail);
//        binding.buyNowBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                databaseReference.child("Admin").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.hasChild("payment")) {
//                            Toast.makeText(goto_buyActivity.this, "Paytm is here", Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            Toast.makeText(goto_buyActivity.this, "Paytm is not here", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(), rozarpayActivity.class);
//                            intent.putExtra("name", coursename);
//                            intent.putExtra("price", price);
//                            intent.putExtra("thumbnail", courseThumbnail);
//                            intent.putExtra("uniqueKey", uniqueKey);
//                            intent.putExtra("admin", adminKey);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });
        binding.buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Admin").child(adminKey).child("Course").child(adminKey).child(uniqueKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            launchedOrNot = snapshot.child("launched").getValue(boolean.class);
                            if (launchedOrNot == true) {
                                FirebaseDatabase.getInstance().getReference().child("Admin").child(adminKey).child("Course").child(adminKey).child(uniqueKey).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (snapshot.hasChild("purchase"))
                                        {
                                            FirebaseDatabase.getInstance().getReference().child("Admin").child(adminKey).child("Course").child(adminKey).child(uniqueKey).child("purchase").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists())
                                                    {
                                                        String ourId = snapshot.child("auth").getValue(String.class);
                                                        if (ourId.equals(FirebaseAuth.getInstance().getUid()))
                                                        {
                                                            Intent intent = new Intent(getApplicationContext(), courseDetailsActivity.class);
                                                            intent.putExtra("course_name",coursename);
                                                            intent.putExtra("uniqueKey",uniqueKey);
                                                            intent.putExtra("thumbnail",courseThumbnail);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent);
                                                        }else{
//                                                            Intent intent = new Intent(getApplicationContext(), rozarpayActivity.class);
//                                                            intent.putExtra("uniqueKey", uniqueKey);
//                                                            intent.putExtra("course_name", uniqueKey);
//                                                            intent.putExtra("course_price", uniqueKey);
//                                                            intent.putExtra("thumbnail", uniqueKey);
//                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                             startActivity(intent);
                                                            Toast.makeText(goto_buyActivity.this, "Buy now", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }else{
                                                        Intent intent = new Intent(getApplicationContext(), rozarpayActivity.class);
                                                        intent.putExtra("course_name",coursename);
                                                        intent.putExtra("course_price",price);
                                                        intent.putExtra("uniqueKey",uniqueKey);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }else{
                                            Intent intent = new Intent(getApplicationContext(), rozarpayActivity.class);
                                            intent.putExtra("course_name",coursename);
                                            intent.putExtra("course_price",price);
                                            intent.putExtra("uniqueKey",uniqueKey);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getApplicationContext(), "Error is \n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "Course coming soon :) !!! ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Error is \n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}