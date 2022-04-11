package com.example.ambulance.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ambulance.Calls;
import com.example.ambulance.R;

import com.example.ambulance.ShowCall;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CallList extends Fragment {


   private ListView callsView;
   private ArrayAdapter<String> adapter;
   private List<String>  listData;
   private List<Calls>  listTemp;

   private String CALL_KEY = "Calls";
   private DatabaseReference mDataBase;
   String Number ="";
   String number;


   public CallList() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calls, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Number = getArguments().getString("Number");

        init(view);
        getDataFromDb(view);
        setOnClickItem();
    }

    private void init(View view)
    {
        callsView = view.findViewById(R.id.CallsView); // инициализация списка
        listData = new ArrayList<>(); // создание списка вызовов
        listTemp = new ArrayList<>(); // созданиие списка с полной информацией о вызовах
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,listData); //инициализация адаптера
        callsView.setAdapter(adapter);
        mDataBase = FirebaseDatabase.getInstance().getReference(CALL_KEY); // подключение к БД
    }
    private void getDataFromDb(View view)
    {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) // Метод для чтения списка вызовов
            {
                if (listData.size() > 0) {listData.clear();} // Очистка списка если элементов больше 0
                if (listTemp.size() > 0) {listTemp.clear();} //
                for (DataSnapshot ds: snapshot.getChildren())  // перебор всех данных в базе
                {
                    TextView numb = view.findViewById(R.id.NumberBrigade);
                    number = numb.getText().toString();
                    if (number.equals("TextView")) {
                        numb.setText(Number); // назначение номера бригады в текстовое поле
                    }
                    else{Number = numb.getText().toString();}


                    Calls call = ds.getValue(Calls.class); // получение данных о вызове
                    assert  call != null;
                    if (call.Brigade_number.equals(Number) && !(call.Status.equals("Завершен"))) // отбор данных по номеру бригады и статусу
                    {
                        String a = ds.getKey();
                        call.Key = a; // Запись ключа текущих данных из базы
                        listData.add(call.Adress + " | " + call.Date + " " + call.Time + " | " + call.Status); // заполнение данных  в список
                        listTemp.add(call); // заполнение данных в список
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Ошибка базы", Toast.LENGTH_LONG).show();
            }
        };
        mDataBase.addValueEventListener(vListener);
    }
    private  void setOnClickItem()
    {
        callsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Calls call = listTemp.get(i);
                Intent intent = new Intent(getContext(), ShowCall.class);
                intent.putExtra("call_key", call.Key);
                intent.putExtra("call_firstName", call.Firstname);
                intent.putExtra("call_lastName", call.Lastname);
                intent.putExtra("call_secoondName", call.Secondname);
                intent.putExtra("call_adress", call.Adress);
                intent.putExtra("call_brigade", call.Brigade_number);
                intent.putExtra("call_age", call.Age);
                intent.putExtra("call_phone", call.Phone);
                intent.putExtra("call_date", call.Date);
                intent.putExtra("call_time", call.Time);
                intent.putExtra("call_description", call.Description);
                intent.putExtra("call_status", call.Status);
                startActivity(intent);

            }
        });
    }
}