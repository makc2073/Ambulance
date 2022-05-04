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
    Calls call;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e("Service","Start...");

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        number = intent.getStringExtra("number");
                        mDataBase = FirebaseDatabase.getInstance().getReference(CALL_KEY);
                        brigade = mDataBase.orderByChild("Brigade_number").equalTo(number);
                        getDataFromDb();
                        NotificationChannel channel = new NotificationChannel(
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
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                sound = MediaPlayer.create(Foreground.this, R.raw.notify);
                Log.e("Service", "BD... Добавление");
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(Foreground.this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_call)
                                .setContentTitle("Есть вызов!")
                                .setPriority(NotificationCompat.PRIORITY_HIGH).setAutoCancel(true);
                NotificationManagerCompat notificationManager =
                        NotificationManagerCompat.from(Foreground.this);
                notificationManager.notify(101, builder.build());
                sound.start();
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
