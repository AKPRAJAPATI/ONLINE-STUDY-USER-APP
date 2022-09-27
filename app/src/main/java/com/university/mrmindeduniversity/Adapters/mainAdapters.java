package com.university.mrmindeduniversity.Adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.university.mrmindeduniversity.Application.courseDetailsActivity;
import com.university.mrmindeduniversity.Models.courseModel;
import com.university.mrmindeduniversity.R;
import com.university.mrmindeduniversity.buyCourse.goto_buyActivity;
import com.university.mrmindeduniversity.databinding.ListitemBinding;

import java.util.ArrayList;

public class mainAdapters extends RecyclerView.Adapter<mainAdapters.mainViewHolder> {

    private Context context;
    private ArrayList<courseModel> arrayList;
    private boolean launchedOrNot;

    public mainAdapters(Context context, ArrayList<courseModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public mainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new mainViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull mainViewHolder holder, int position) {
        courseModel model = arrayList.get(position);
        SharedPreferences sharedPreferences = context.getSharedPreferences("mrMinded", MODE_PRIVATE);
        String adminKey = sharedPreferences.getString("key", "");
        Picasso.get().load(model.getThumbnail()).into(holder.binding.courseThumbnail);
        holder.binding.coursePrice.setText(String.valueOf("Price :- " + model.getPrice()));
        holder.binding.courseName.setText(model.getCourse_name());

        /////////  THIS IS MADE FOR CHECK LAUNCHED OR NOT ///////////////////////////////////////
        FirebaseDatabase.getInstance().getReference().child("Admin").child(adminKey).child("Course").child(adminKey).child(model.getUniquekey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    launchedOrNot = snapshot.child("launched").getValue(boolean.class);
                    if (launchedOrNot == true) {
                        holder.binding.nameLayout.setVisibility(View.VISIBLE);
                        holder.binding.priceLayout.setVisibility(View.VISIBLE);
                        holder.binding.messageLayout.setVisibility(View.GONE);

                        FirebaseDatabase.getInstance().getReference().child("Admin").child(adminKey).child("Course").child(adminKey).child(model.getUniquekey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.hasChild("purchase"))
                                {
                                    FirebaseDatabase.getInstance().getReference().child("Admin").child(adminKey).child("Course").child(adminKey).child(model.getUniquekey()).child("purchase").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists())
                                            {
                                                String ourId = snapshot.child("auth").getValue(String.class);
                                                if (ourId.equals(FirebaseAuth.getInstance().getUid()))
                                                {
                                                    holder.binding.nameLayout.setVisibility(View.VISIBLE);
                                                    holder.binding.priceLayout.setVisibility(View.GONE);
                                                    holder.binding.messageLayout.setVisibility(View.GONE);
                                                }else{
                                                    holder.binding.nameLayout.setVisibility(View.VISIBLE);
                                                    holder.binding.priceLayout.setVisibility(View.VISIBLE);
                                                    holder.binding.messageLayout.setVisibility(View.GONE);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }else{
                                    holder.binding.nameLayout.setVisibility(View.VISIBLE);
                                    holder.binding.priceLayout.setVisibility(View.VISIBLE);
                                    holder.binding.messageLayout.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(context, "Error is \n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        holder.binding.nameLayout.setVisibility(View.VISIBLE);
                        holder.binding.messageLayout.setVisibility(View.VISIBLE);

                        holder.binding.priceLayout.setVisibility(View.GONE);
                    }
                } else {
                    //snapshot is not exits
                    Toast.makeText(context, "akashkumar g", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error is \n " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        /////// THIS IS MADE FOR CLICK COURSE LAUNCHED

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Admin").child(adminKey).child("Course").child(adminKey).child(model.getUniquekey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            launchedOrNot = snapshot.child("launched").getValue(boolean.class);
                            if (launchedOrNot == true) {
                                FirebaseDatabase.getInstance().getReference().child("Admin").child(adminKey).child("Course").child(adminKey).child(model.getUniquekey()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (snapshot.hasChild("purchase"))
                                        {
                                            FirebaseDatabase.getInstance().getReference().child("Admin").child(adminKey).child("Course").child(adminKey).child(model.getUniquekey()).child("purchase").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists())
                                                    {
                                                        String ourId = snapshot.child("auth").getValue(String.class);
                                                        if (ourId.equals(FirebaseAuth.getInstance().getUid()))
                                                        {
                                                            Intent intent = new Intent(context, courseDetailsActivity.class);
                                                            intent.putExtra("course_name",model.getCourse_name());
                                                            intent.putExtra("uniqueKey",model.getUniquekey());
                                                            intent.putExtra("thumbnail",model.getThumbnail());
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            context.startActivity(intent);
                                                        }else{
                                                            Intent intent = new Intent(context, goto_buyActivity.class);
                                                            intent.putExtra("uniqueKey", model.getUniquekey());
                                                            intent.putExtra("course_name", model.getCourse_name());
                                                            intent.putExtra("course_price", model.getPrice());

                                                            intent.putExtra("thumbnail", model.getThumbnail());
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            context.startActivity(intent);
                                                        }
                                                    }else{
                                                        Intent intent = new Intent(context, goto_buyActivity.class);
                                                        intent.putExtra("uniqueKey", model.getUniquekey());
                                                        intent.putExtra("course_name", model.getCourse_name());
                                                        intent.putExtra("course_price", model.getPrice());
                                                        intent.putExtra("thumbnail", model.getThumbnail());
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        context.startActivity(intent);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }else{
                                            Intent intent = new Intent(context, goto_buyActivity.class);
                                            intent.putExtra("uniqueKey",model.getUniquekey());
                                            intent.putExtra("course_name",model.getCourse_name());
                                            intent.putExtra("course_price",model.getPrice());
                                            intent.putExtra("thumbnail",model.getThumbnail());

                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            context.startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(context, "Error is \n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } else {
                                Toast.makeText(context, "Course coming soon :) !!! ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Error is \n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class mainViewHolder extends RecyclerView.ViewHolder {
        private ListitemBinding binding;

        public mainViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ListitemBinding.bind(itemView);
        }
    }
}
