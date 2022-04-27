package com.example.ambulance.ui.Chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ambulance.Message;
import com.example.ambulance.MessageAdapter;
import com.example.ambulance.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class UserChat extends Fragment {
    String login;
    private ConstraintLayout fragment_chat;
    ArrayList<Message> getMes = new ArrayList<Message>();
    String text;
    ListView MessageList;
    private DatabaseReference mDataBase;
    private List<Message> ListMes;

    public UserChat() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragment_chat = view.findViewById(R.id.Fragmen_chat);
        mDataBase = FirebaseDatabase.getInstance().getReference("Message");

        login = getArguments().getString("Login");
        EditText MessageText = view.findViewById(R.id.messageEdt);

        ListMes = new ArrayList<>();
        Button send = view.findViewById(R.id.SendBtn);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS").format(new Date()); // назначение ключа сообщений
                String time;
                text = MessageText.getText().toString(); // присвоение текста сообщения из поля ввода
                Message message = new Message(login, text, time = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date())); // создание нового сообщения
                mDataBase.child(key).setValue(message); // добавление сообщения в класс
                MessageText.setText("");
            }
        });

        listnerMes(view);
    }
public void listnerMes(View view){
    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            if(getMes.size() > 0){getMes.clear();}
            for(DataSnapshot ds: dataSnapshot.getChildren()) { // Выбор всех сообщений из базы
                Message mes = ds.getValue(Message.class); // заполнение сообщений в класс
                assert mes != null;
                getMes.add(new Message(mes.userName, mes.textMessage, mes.messageTime));// начальная инициализация списка
            }
            // получаем элемент ListView
            MessageList = view.findViewById(R.id.ListMessage);
            // создаем адаптер
            MessageAdapter stateAdapter = new MessageAdapter(getContext(), R.layout.item_message, getMes); // Адапетр для добавления данных в спискок
            // устанавливаем адаптер
            MessageList.setAdapter(stateAdapter);
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    mDataBase.addValueEventListener(postListener);
}


}