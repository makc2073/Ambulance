package com.example.ambulance;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;

import android.os.Build;
import android.os.IBinder;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
public class Foreground  extends Service {
    final String CHANNEL_ID = "Foreground Service ID";
    private String CALL_KEY = "Calls";
    private DatabaseReference mDataBase;
    Query brigade;
    MediaPlayer sound;
    String number;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) // метод который запучкает работу сервиса
    {
        Log.e("Service","Start..."); // сообщении о старте работы сервиса
        new Thread( // запуск работы потока
                new Runnable() {
                    @Override
                    public void run() {
                        number = intent.getStringExtra("number"); // получения номера бриагады
                        mDataBase = FirebaseDatabase.getInstance().getReference(CALL_KEY);// получение ссылки базы данных с вызовами
                        brigade = mDataBase.orderByChild("Brigade_number").equalTo(number); // выборка по номеру бригады
                        getDataFromDb();
                        NotificationChannel channel = new NotificationChannel( // создание уведомления который сообщает о работе уведомлений и указывет номер бригады
                                CHANNEL_ID,
                                CHANNEL_ID,
                                NotificationManager.IMPORTANCE_MIN);
                        getSystemService(NotificationManager.class).createNotificationChannel(channel);
                        Notification.Builder notification = new Notification.Builder(Foreground.this, CHANNEL_ID)
                                .setContentTitle("Работает| Бригада: " + number).setSmallIcon(R.drawable.ic_hospital).setAutoCancel(true).setVibrate(new long[] { 100, 500, 100, 500, 100, 500 }); // автоматически закрыть уведомление после нажатия;
                        startForeground(1001, notification.build());
                        Log.e("Service", "BD... Добавление");
                    }
                }
        ).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    private void getDataFromDb()
    {
        //
       ChildEventListener childEventListener = new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) { // метод который срабатывает при добваление вызова

                sound = MediaPlayer.create(Foreground.this, R.raw.notify);// звук уведомления
                Log.e("Service", "BD... Добавление"); // сообщении о срабатывании
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(Foreground.this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_call)
                                .setContentTitle("Есть вызов!")
                                .setPriority(NotificationCompat.PRIORITY_HIGH).setAutoCancel(true);
                NotificationManagerCompat notificationManager =
                        NotificationManagerCompat.from(Foreground.this);
                notificationManager.notify(101, builder.build()); // создание уведомления о вызове котоырй был добавлен
                sound.start(); // запуск звука
            }

           @RequiresApi(api = Build.VERSION_CODES.O)
           @Override
           public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                   Calls call = snapshot.getValue(Calls.class); // получение данных о вызове

               sound = MediaPlayer.create(Foreground.this, R.raw.notify);
               Log.e("Service", "BD... Изменение");
               NotificationCompat.Builder builder =
                       new NotificationCompat.Builder(Foreground.this, CHANNEL_ID)
                               .setSmallIcon(R.drawable.ic_call)
                               .setContentTitle(call.Adress + " Вызов изменен")
                               .setPriority(NotificationCompat.PRIORITY_HIGH).setAutoCancel(true);
               NotificationManagerCompat notificationManager =
                       NotificationManagerCompat.from(Foreground.this);
               notificationManager.notify(101, builder.build());
               sound.start();
           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot snapshot) {

           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

           }

           @RequiresApi(api = Build.VERSION_CODES.O)
           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       };brigade.addChildEventListener(childEventListener);
    }
}
