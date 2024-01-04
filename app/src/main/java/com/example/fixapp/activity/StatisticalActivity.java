package com.example.fixapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fixapp.R;
import com.example.fixapp.adapter.StatisticalAdapter;
import com.example.fixapp.model.Order;
import com.example.fixapp.model.Statistical;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatisticalActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView nullData,totalMoney,total_quantity;
    private TextInputEditText edStartDate;
    private ImageView imgStartDate;
    private TextInputEditText edEndDate;
    private ImageView imgEndDate;
    private Button btnQuerry;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
    private String startDate,endDate;
    int mYear, mMonth, mDate;
    private List<Order> orderList = new ArrayList<>();
    private List<Statistical> statisticalList=  new ArrayList<>();
    private StatisticalAdapter adapter;
    private FirebaseUser firebaseUser;
    int totalQuantitySum = 0;
    double totalAmountSum = 0;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);
        anhXa();
        setDay();

        btnQuerry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edStartDate.getText().toString())||TextUtils.isEmpty(edEndDate.getText().toString())){
                    Toast.makeText(StatisticalActivity.this, "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
                }else {

                        startDate = edStartDate.getText().toString();
                        endDate = edEndDate.getText().toString();
                        getDataOrder(startDate,endDate);
                        recyclerView.setAdapter(adapter);

                }
            }
        });

    }
    private void anhXa(){
        recyclerView = findViewById(R.id.rcv_statistical);
        nullData = findViewById(R.id.nullDataStatistical);
        total_quantity = findViewById(R.id.total_quantity);
        totalMoney = findViewById(R.id.totalMoney);
        edStartDate = findViewById(R.id.ed_start_date);
        imgStartDate = findViewById(R.id.img_start_date);
        edEndDate = findViewById(R.id.ed_end_date);
        imgEndDate = findViewById(R.id.img_end_date);
        btnQuerry = findViewById(R.id.btnQuerry);
        edStartDate.setFocusable(false);
        edStartDate.setFocusableInTouchMode(false);
        edEndDate.setFocusable(false);
        edEndDate.setFocusableInTouchMode(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new StatisticalAdapter(getApplicationContext(),statisticalList);
    }

    private void getDataOrder(String startDay,String endDay){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String id_user = firebaseUser.getUid();
        DatabaseReference databaseReference = firebaseDatabase.getReference("list_order");
        Query query = databaseReference.orderByChild("date")
                .startAt(startDay)
                .endAt(endDay + " 23:59:59");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,Statistical> statisticalMap = new HashMap<>();
                int totalQuantitySum = 0;
                double totalAmountSum = 0;
               if (snapshot.exists()){
                   for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                       Order order = transactionSnapshot.getValue(Order.class);
//                       if (order.getStatus().equals("done")&&order.getIdSeller().equals(id_user)){
////                           Statistical statistical = statisticalMap.get(order.getIdProduct());
////                           if (statistical == null) {
////
////                               statistical = new Statistical(order.getIdProduct(),order.getImgProduct(), order.getNameProduct(), order.getQuantity(),order.getTotal());
////                               statisticalMap.put(order.getIdProduct(), statistical);
////                           } else {
////
////                               statistical.setTotalQuantity(statistical.getTotalQuantity() + order.getQuantity());
////                               statistical.setTotalAmount(statistical.getTotalAmount() + order.getTotal());
////                           }
////                           totalQuantitySum += order.getQuantity();
////                           totalAmountSum += order.getTotal();
//                       }else {
//                           Log.d("=======", "onDataChange: Không có");
//                       }
                   }
               }
                if (statisticalMap.isEmpty()) {
                    nullData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    nullData.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    total_quantity.setText(String.valueOf(totalQuantitySum));
                    totalMoney.setText(String.valueOf(totalAmountSum));
                    statisticalList.clear();
                    statisticalList.addAll(statisticalMap.values());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void setDay(){
        DatePickerDialog.OnDateSetListener FromDate = (datePicker, year, monthOfYear, dayOfMonth) -> {
            mYear = year;
            mMonth = monthOfYear;
            mDate = dayOfMonth;

            GregorianCalendar calendar = new GregorianCalendar(mYear, mMonth, mDate);
            edStartDate.setText(dateFormat.format(calendar.getTime()));
        };
        DatePickerDialog.OnDateSetListener ToDate = (datePicker, year, monthOfYear, dayOfMonth) -> {
            mYear = year;
            mMonth = monthOfYear;
            mDate = dayOfMonth;

            GregorianCalendar calendar = new GregorianCalendar(mYear, mMonth, mDate);
            edEndDate.setText(dateFormat.format(calendar.getTime()));
        };
        imgStartDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDate = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog_start = new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar,FromDate, mYear, mMonth, mDate);
            dialog_start.show();
        });
        imgEndDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDate = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog_end = new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar,ToDate,mYear, mMonth, mDate);
            dialog_end.show();
        });
    }

}