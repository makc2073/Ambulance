package com.example.ambulance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.ambulance.ui.Chat.UserChat;
import com.example.ambulance.ui.Call.CallList;
import com.example.ambulance.ui.Profile.UsersProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    Fragment CallsFragment = new CallList();
    Fragment UserProfileFragment = new UsersProfile();
    Fragment ChatFragment = new UserChat();
    Fragment nowFragment;
    private DatabaseReference mDatabase;

    Users user = new Users();
    String login, number, password, fname,lname;
    TextView profilename, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button exit = (Button) findViewById(R.id.BtnExit);
        exit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference("UserList");
        getDataFromDb();
        Bundle arguments = getIntent().getExtras();
        if(arguments!=null)
        {
            user = (Users) arguments.getSerializable(Users.class.getSimpleName());
            number = user.Brigades_number1;
            login= user.Login1;
            password = user.Password1;
            fname = user.FirstName;
            lname = user.LastName;
        }
        profilename = findViewById(R.id.ProfilName);
        profilename.setText("Пользователь: " + login);
        username = findViewById(R.id.HeaderName);
    }
    public void Calls(View view)
    {
        if (nowFragment != CallsFragment)
        {
            Bundle bundle = new Bundle();
            bundle.putString("Number", number);
            CallsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerViewMain, CallsFragment)
                    .commit();
        }
        nowFragment = CallsFragment;
    }
    public void Profile(View view)
    {
        if (nowFragment != UserProfileFragment)
        {
            Bundle bundle = new Bundle();
            bundle.putString("Login", login);
            UserProfileFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerViewMain, UserProfileFragment)
                    .commit();

        }
        nowFragment = UserProfileFragment;
    }
    public void Chat(View view)
    {
        if (nowFragment != ChatFragment)
        {
            Bundle bundle = new Bundle();
            bundle.putString("Login", login);
            ChatFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerViewMain, ChatFragment)
                    .commit();
        }
        nowFragment = ChatFragment;
    }
    private void getDataFromDb()
    {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                for (DataSnapshot ds: snapshot.getChildren())
                {
                    Users user = ds.getValue(Users.class);
                    assert  user != null;
                    if (user.Login1.equals(login))
                    {
                        username.setText("имя: " + user.FirstName + " " + user.LastName);
                        number = user.Brigades_number1;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        mDatabase.addValueEventListener(vListener);
    }
}
