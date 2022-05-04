package com.example.ambulance;



import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    Fragment LogFragment = new LoginUser();
    Fragment RegFragment = new RegisterUser();


    Fragment nowFragment;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
