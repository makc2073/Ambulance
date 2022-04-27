package com.example.ambulance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginUser extends Fragment {
    private static final String PREFS_FILE = "Account";
    private static final String PREF_LOGIN = "login";
    private static final String PREF_PASS = "pass";
    SharedPreferences settings;

    private String USER_KEY = "UserList";
    private DatabaseReference mDataBase;
    public String login, password;
    public String BdLogin, Bdpass, Bdnumber, BdFirstName,BdLastName;

    public LoginUser() {
        // Required empty public constructor
    }

     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settings = this.getActivity().getSharedPreferences("login", getContext().MODE_PRIVATE);

        EditText loginEd = (EditText) view.findViewById(R.id.LoginEdt);
        EditText passwordED = (EditText) view.findViewById(R.id.PasswordEdtText);

        String log = settings.getString(PREF_LOGIN,"");
        loginEd.setText(log);
        String pass = settings.getString(PREF_PASS,"");
        passwordED.setText(pass);

        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

       Button enterBtn = (Button) view.findViewById(R.id.EnterBtn);
        enterBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                login =  loginEd.getText().toString(); // переменные с логин/пароль
                password = passwordED.getText().toString();

                mDataBase.child(login).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Ошибка в подключении", Toast.LENGTH_LONG).show();
                        }
                        else {
                            try {
                                Users user = task.getResult().getValue(Users.class); // получение данных о пользователе в класс
                                BdLogin = user.Login1;
                                Bdpass = user.Password1;
                                Bdnumber = user.Brigades_number1;
                                BdFirstName = user.FirstName;
                                BdLastName = user.LastName;
                            }catch (Exception ex){Toast.makeText(getActivity(), "Пользователя не существует", Toast.LENGTH_LONG).show();}

                            if (login.equals(BdLogin) && password.equals(Bdpass)) // проверка правильности логина и пароля
                            {
                                Users userProfile = new Users(Bdnumber,BdLogin,Bdpass,BdFirstName,BdLastName);
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                intent.putExtra(Users.class.getSimpleName(), userProfile); // передача данных о пользователе в главное окно

                                // сохраняем его в настройках
                                SharedPreferences.Editor prefEditor = settings.edit();
                                prefEditor.putString(PREF_LOGIN, login);
                                prefEditor.putString(PREF_PASS, password);
                                prefEditor.apply();

                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Неправильный логин или пароль", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });


            }
        });
    }

}