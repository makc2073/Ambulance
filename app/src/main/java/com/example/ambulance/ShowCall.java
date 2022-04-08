package com.example.ambulance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShowCall extends AppCompatActivity {
private TextView firstNameTv, adressTv, brigadeTv, ageTv, phoneTv, dateTv, descriptionTv;
boolean status = false;
String stat, key;
private DatabaseReference mDatabase;
Button acceptCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_show);
        init();
        getIntentCalllist();
    }
    private void init()
    {
        firstNameTv = findViewById(R.id.FirstNameText);
        adressTv = findViewById(R.id.AdressText);
        brigadeTv = findViewById(R.id.BrigadeText);
        ageTv = findViewById(R.id.AgeText);
        phoneTv = findViewById(R.id.PhoneText);
        dateTv = findViewById(R.id.DateText);
        descriptionTv = findViewById(R.id.DescriptionText);
        acceptCall = findViewById(R.id.AcceptCall);

    }
    public void acceptCall(View view)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("Calls");
        String textAccept = acceptCall.getText().toString();
        if(textAccept.equals("Принят") || textAccept.equals("Завершить вызов"))
        {
            acceptCall.setText("Завершен");
            mDatabase.child(key).child("Status").setValue("Завершен");        }
        else
        {
            acceptCall.setText("Завершить вызов");
            mDatabase.child(key).child("Status").setValue("Принят");
        }

    }
    private void getIntentCalllist()
    {
        Intent intent = getIntent();
        if(intent != null)
        {
            firstNameTv.setText(intent.getStringExtra("call_firstName") + " " + intent.getStringExtra("call_lastName") + " "+intent.getStringExtra("call_secoondName"));
            adressTv.setText(intent.getStringExtra("call_adress"));
            brigadeTv.setText(intent.getStringExtra("call_brigade"));
            ageTv.setText(intent.getStringExtra("call_age"));
            phoneTv.setText(intent.getStringExtra("call_phone"));
            dateTv.setText(intent.getStringExtra("call_date") + " " + intent.getStringExtra("call_time"));
            descriptionTv.setText(intent.getStringExtra("call_description"));
            stat = intent.getStringExtra("call_status");
            key = intent.getStringExtra("call_key");
            if(stat.equals("Принят"))
            {
                acceptCall.setText("Завершить вызов");
            }
            if(stat.equals("Завершен"))
            {
                acceptCall.setText("Завершен");
            }
        }
    }
}