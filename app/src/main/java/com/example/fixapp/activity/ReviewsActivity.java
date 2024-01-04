package com.example.fixapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixapp.MainActivity;
import com.example.fixapp.R;
import com.example.fixapp.model.Reviews;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReviewsActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    private ImageButton img_back;
    private ImageView imgPr,colorPr;
    private TextView namepr,quantityPr;
    private TextInputEditText edReviews;
    private Button btnsend;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mReference;
    private String idProduct;
    private int numStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        anhXa();
        Intent intent = getIntent();
        String id_Order = intent.getStringExtra("idOrder");
        getDataOrder(id_Order);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                numStart=Math.round(v);
            }
        });

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(edReviews.getText().toString())){
                    Toast.makeText(ReviewsActivity.this, "Hãy viết đánh giá", Toast.LENGTH_SHORT).show();
                } else if (numStart==0) {
                    Toast.makeText(ReviewsActivity.this, "Hãy chọn số sao để đánh giá sự hài lòng của bạn", Toast.LENGTH_SHORT).show();
                }else {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reviewRef = database.getReference("reviews");
                    firebaseUser = firebaseAuth.getCurrentUser();
                    String idReview = reviewRef.push().getKey();
                    String idUser = firebaseUser.getUid();
                    sendReviews(idReview,id_Order,idProduct,numStart,edReviews.getText().toString());
                    Toast.makeText(ReviewsActivity.this, "Đánh giá thành công", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(ReviewsActivity.this, MainActivity.class);
                    startActivity(intent1);
                }
            }
        });
    }
    private void anhXa(){
        ratingBar =findViewById(R.id.start);
        img_back = findViewById(R.id.img_back);
        imgPr = findViewById(R.id.imgPr);
        colorPr = findViewById(R.id.colorPr);
        namepr = findViewById(R.id.namePr);
        quantityPr = findViewById(R.id.quantityPr);
        edReviews = findViewById(R.id.tvReviews);
        btnsend = findViewById(R.id.btnsendreviews);
        mReference = FirebaseDatabase.getInstance().getReference();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void sendReviews(String id,String idOrder,String idProduct,int start, String cmt){
        String idUser = firebaseUser.getUid();
        DatabaseReference userRef = mReference.child("user").child(idUser);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String img = snapshot.child("img").getValue(String.class);
                String name = snapshot.child("username").getValue(String.class);
                String date = getCurrentTime();
                Reviews reviews = new Reviews(id,idUser,name,img,idOrder,idProduct,start,cmt,date);
                AddReviews(reviews);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void AddReviews(Reviews reviews) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("reviews");
        String id = reviews.getId();
        myRef.child(id).setValue(reviews, (error, ref) -> {
            if (error == null) {
            } else {
                Toast.makeText(ReviewsActivity.this, "Lỗi khi lưu reviews vào Realtime Database", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getDataOrder(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("list_order");
        reference.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    String idOrder = orderSnapshot.child("id").getValue(String.class);
                    if (idOrder != null && idOrder.equals(id)) {
                        String name = orderSnapshot.child("nameProduct").getValue(String.class);
                        int quantity = orderSnapshot.child("quantity").getValue(Integer.class);
                        String img = orderSnapshot.child("imgProduct").getValue(String.class);
                        int color = orderSnapshot.child("color").getValue(Integer.class);
                        double total = orderSnapshot.child("total").getValue(Double.class);
                        idProduct = orderSnapshot.child("idProduct").getValue(String.class);

                        namepr.setText("Tên :"+name);
                        quantityPr.setText("Số lượng : "+quantity);
                        colorPr.setBackgroundColor(color);
                        if (img != null && !img.isEmpty()) {
                            Uri imageUri = Uri.parse(img);
                            Picasso.get()
                                    .load(imageUri)
                                    .placeholder(R.drawable.pant)
                                    .error(R.drawable.pant)
                                    .into(imgPr);
                        } else {
                            imgPr.setImageResource(R.drawable.pant);
                        }

                    } else {
                        Toast.makeText(ReviewsActivity.this, "NULLLL", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("===", "onCancelled: Error retrieving order data", error.toException());
            }
        });
    }
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date currentDate = new Date();
        return sdf.format(currentDate);
    }
}