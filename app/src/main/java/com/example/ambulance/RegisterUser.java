package com.example.ambulance;

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


public class RegisterUser extends Fragment {

    private DatabaseReference mDatabase;
    public String Login,Password,RePassword,Number, userId;
    public EditText LoginEd,PasswordEd,RePasswordEd,NumberEd;


    public RegisterUser() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mDatabase = FirebaseDatabase.getInstance().getReference("UserList");
        FirebaseDatabase database = FirebaseDatabase.getInstance();


         LoginEd = (EditText) view.findViewById(R.id.LoginEdt);
         PasswordEd = (EditText) view.findViewById(R.id.PasswordEdtText);
         RePasswordEd = (EditText) view.findViewById(R.id.RePasswordEdt);
         NumberEd = (EditText) view.findViewById(R.id.NumberEdt);
         Button Accept = (Button) view.findViewById(R.id.AcceptBtn);
         Accept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                Login = LoginEd.getText().toString();
                Password = PasswordEd.getText().toString();
                RePassword = RePasswordEd.getText().toString();
                Number = NumberEd.getText().toString();
                userId = Login;

                mDatabase.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task)
                    {
                        if (!task.isSuccessful())
                        {
                            Toast.makeText(getActivity(), "Ошибка в подключении", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            try
                            {
                                Users user = task.getResult().getValue(Users.class);
                                if (Login.equals(user.Login1))
                                {
                                    Toast.makeText(getActivity(), "Пользователь существует", Toast.LENGTH_LONG).show();
                                }
                            }
                            catch (Exception ex)
                            {

                                if (Login.equals("") || Password.equals("") || Number.equals(""))
                                {
                                    Toast.makeText(getActivity(), "Заполните все поля", Toast.LENGTH_LONG).show();
                                }
                                else
                                {

                                    if (Password.equals(RePassword))
                                    {

                                        Users user = new Users(Number, Login, Password, "","");
                                        mDatabase.child(userId).setValue(user);
                                        Toast.makeText(getActivity(), "Пользователь добавлен", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(), "Пароли не совпадают!", Toast.LENGTH_LONG).show();
                                    }

                                }
                            }

                        }
                    }
                });
            }
        });


    }

}