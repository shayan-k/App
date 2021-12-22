package com.example.myapp_sherkat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.myapp_sherkat.R;
import com.example.myapp_sherkat.classs.ApiClient;
import com.example.myapp_sherkat.interfaces.ApiInterface;
import com.example.myapp_sherkat.classs.SharedPrefManager;
import com.example.myapp_sherkat.classs.User;
import com.example.myapp_sherkat.classs.CustomToast;


public class SignLoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

      NavController navController1=Navigation.findNavController(this,R.id.fragment);
      NavigationUI.setupActionBarWithNavController(this,navController1);




    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }


}