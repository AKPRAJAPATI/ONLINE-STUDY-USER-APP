package com.university.mrmindeduniversity.buyCourse;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.university.mrmindeduniversity.R;
import com.university.mrmindeduniversity.databinding.ActivityBuyCourse2Binding;

import org.json.JSONObject;

import java.util.HashMap;

public class rozarpayActivity extends AppCompatActivity implements PaymentResultListener {
    private ActivityBuyCourse2Binding binding;

    private String testIdRozarpay = "rzp_test_JbfXbnNqN8SJ4M";
    private String originalIdRozarpay = "rzp_live_jZBtWnpOO4RoNe";
    private String courseName;
    private String uniqueKey;
    private String thumbnail;
    private String adminKey;
    private String aadminKey;
    private String appName = "Mr Minded University";
    private int price;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuyCourse2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());  // activity buy course 2

        auth = FirebaseAuth.getInstance();
        firebaseUser =auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Checkout.preload(getApplicationContext());

        courseName = getIntent().getStringExtra("course_name");
        thumbnail = getIntent().getStringExtra("thumbnail");
        uniqueKey = getIntent().getStringExtra("uniqueKey");
        adminKey = getIntent().getStringExtra("admin");
        price = getIntent().getIntExtra("course_price", 0);
        String prices = String.valueOf(price);

        binding.appName.setText(appName);


        binding.coursenameBuy.setText("Course Name :- "+courseName);
        binding.coursePriceBuy.setText("Price :- "+prices);

        binding.buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.courseEmailBuy.getText().toString().equals(""))
                {
                    binding.courseEmailBuy.requestFocus();
                    Toast.makeText(rozarpayActivity.this, "Email is empty", Toast.LENGTH_SHORT).show();
                }else if (!binding.courseEmailBuy.getText().toString().contains("@gmail.com"))
                {
                    binding.courseEmailBuy.requestFocus();
                    Toast.makeText(rozarpayActivity.this, "Email is wrong", Toast.LENGTH_SHORT).show();
                }else if (binding.courseNumberBuy.getText().toString().equals(""))
                {
                    binding.courseNumberBuy.requestFocus();
                    Toast.makeText(rozarpayActivity.this, "Number is empty", Toast.LENGTH_SHORT).show();
                }else if (binding.courseNumberBuy.getText().toString().length() < 10 || binding.courseNumberBuy.getText().toString().length() > 12 )
                {
                    binding.courseNumberBuy.requestFocus();
                    binding.courseNumberBuy.setHint("919712345678 like this");
                    Toast.makeText(rozarpayActivity.this, "Number is wrong", Toast.LENGTH_SHORT).show();
                }else{
                  startPayment(binding.courseEmailBuy.getText().toString(), binding.courseNumberBuy.getText().toString());
                }
            }
        });
    }

    private void startPayment(String email, String number) {
        Checkout checkout = new Checkout();
        checkout.setKeyID(originalIdRozarpay);
        //Checkout.preload(getApplicationContext());

        checkout.setImage(R.drawable.mr_rounded);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", appName);
            options.put("description", "buy course");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", price * 100);//pass amount in currency subunits
            options.put("prefill.email", binding.courseEmailBuy.getText().toString());
            options.put("prefill.contact", binding.courseNumberBuy.getText().toString());
            checkout.open(activity, options);

        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        SharedPreferences sharedPreferences = getSharedPreferences("mrMinded", MODE_PRIVATE);
        aadminKey = sharedPreferences.getString("key", "");


        try {
            HashMap<String , Object> hashMap = new HashMap<>();
            hashMap.put("auth",FirebaseAuth.getInstance().getUid());
            hashMap.put("buy",true);
            FirebaseDatabase.getInstance().getReference().child("Admin").child(aadminKey).child("Course").child(aadminKey).child(uniqueKey).child("purchase").child(FirebaseAuth.getInstance().getUid()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        HashMap<String , Object> hashMap = new HashMap<>();
                        hashMap.put("auth",FirebaseAuth.getInstance().getUid());
                        hashMap.put("buy",true);
                        hashMap.put("uniqueKey",uniqueKey);
                        hashMap.put("payment",s.toString());

                        FirebaseDatabase.getInstance().getReference().child("Admin").child("payment").child(FirebaseAuth.getInstance().getUid()).push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(rozarpayActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(rozarpayActivity.this, "Failed "+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error is "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment cancel \n\n "+s, Toast.LENGTH_SHORT).show();
    }
}