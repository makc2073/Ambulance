package com.example.ambulance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    Fragment RegFragment = new RegisterUser();
    Fragment LogFragment = new LoginUser();
    Fragment nowFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nowFragment = LogFragment;
    }
    public void login(View view)
    {
        if (nowFragment != LogFragment) getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, LogFragment).commit();
        nowFragment = LogFragment;
    }
    public void regist(View view)
    {
        if (nowFragment != RegFragment) getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, RegFragment).commit();
        nowFragment = RegFragment;

    }
}
