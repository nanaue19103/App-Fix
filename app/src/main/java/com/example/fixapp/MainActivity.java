package com.example.fixapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;


import com.example.fixapp.database.FireBaseType;

import com.example.fixapp.databinding.ActivityMainBinding;
import com.example.fixapp.fragment.CartFragment;
import com.example.fixapp.fragment.HomeFragment;
import com.example.fixapp.fragment.NotificationFragment;
import com.example.fixapp.fragment.ProfileFragment;

import com.example.fixapp.fragment.SearchProductFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;

    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mReference;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference();
        fragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, homeFragment).commit();
        setRoleListUser();
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (id == R.id.cart) {
                replaceFragment(new CartFragment());
            } else if (id == R.id.profile) {
                replaceFragment(new ProfileFragment());
            } else if (id == R.id.search) {
                replaceFragment(new SearchProductFragment());
            } else if (id==R.id.notification) {
                replaceFragment(new NotificationFragment());
            } else {
                replaceFragment(new HomeFragment());
            }
            return true;
        });
        binding.bottomNavigationViewAdmin.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.home) {
                replaceFragment(new HomeFragment());
            }else if (id == R.id.profile) {
                replaceFragment(new ProfileFragment());
            } else if (id == R.id.search) {
                replaceFragment(new SearchProductFragment());
            } else if (id==R.id.notification) {
                replaceFragment(new NotificationFragment());
            } else {
                replaceFragment(new HomeFragment());
            }
            return true;
        });
    }
    public void setRoleListUser() {
        String id = firebaseUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(id);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isAdmin = FireBaseType.isAdmin(dataSnapshot);
                if (isAdmin) {
                    binding.bottomNavigationViewAdmin.setVisibility(View.VISIBLE);
                } else {
                    binding.bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Loi", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void requestPermissions() {
        boolean cameraPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean storagePermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (!cameraPermissionGranted || !storagePermissionGranted) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            // Quyền đã được cấp, tiếp tục xử lý
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean cameraPermissionGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean storagePermissionGranted = grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED;

            if (!cameraPermissionGranted || !storagePermissionGranted) {
                showPermissionDeniedDialog();
            } else {
                // Quyền đã được cấp, tiếp tục xử lý
            }
        }
    }

    private void showPermissionDeniedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Quyền truy cập bị từ chối");
        builder.setMessage("Ứng dụng không thể hoạt động mà không có quyền truy cập. Vui lòng cấp quyền trong cài đặt.");
        builder.setPositiveButton("Đi đến cài đặt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openAppSettings();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, PERMISSION_REQUEST_CODE);
    }
}