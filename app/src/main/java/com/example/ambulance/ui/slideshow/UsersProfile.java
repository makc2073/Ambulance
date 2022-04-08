package com.example.ambulance.ui.slideshow;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ambulance.R;
import com.example.ambulance.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class UsersProfile extends Fragment {
String Number,Login,Password,Fname,Lname;
    EditText FnameTv,LnameTv,NumberTv;
    private DatabaseReference mDataBase;
    public UsersProfile( ) {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Login = getArguments().getString("Login");
        mDataBase = FirebaseDatabase.getInstance().getReference("UserList");
        mDataBase.child(Login).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful())
                {
                    Toast.makeText(getActivity(), "Ошибка в подключении", Toast.LENGTH_LONG).show();
                }
                else
                {
                        Users user = task.getResult().getValue(Users.class);
                        Password = user.Password1;
                        Number = user.Brigades_number1;
                        Fname = user.FirstName;
                        Lname = user.LastName;

                    TextView loginTv = view.findViewById(R.id.ProfilLogin);
                     FnameTv = view.findViewById(R.id.ProfilFname);
                     LnameTv = view.findViewById(R.id.ProfilLname);
                     NumberTv = view.findViewById(R.id.ProfilNumber);
                    loginTv.setText(Login);
                    FnameTv.setText(Fname);
                    LnameTv.setText(Lname);
                    NumberTv.setText(Number);
                }
            }
        });

Button ProfilEdit = (Button) view.findViewById(R.id.EditBtn);
ProfilEdit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        TextView profilename = view.findViewById(R.id.HeaderName);

        Number = NumberTv.getText().toString();
        Fname = FnameTv.getText().toString();
        Lname = LnameTv.getText().toString();
        //profilename.setText("Имя:" + Fname + " "  + Lname);
        Users user = new Users(Number,Login,Password,Fname,Lname);
        mDataBase.child(Login).setValue(user);
    }
});
    }
}