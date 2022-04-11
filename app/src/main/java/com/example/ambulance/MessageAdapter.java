package com.example.ambulance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    private LayoutInflater inflater;
    private int layout;
    private List<Message> messages;

    public MessageAdapter(Context context, int resource, List<Message> messages) {
        super(context, resource, messages);
        this.messages = messages;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView messageUser = view.findViewById(R.id.messageUser);
        TextView messageText = view.findViewById(R.id.messageText);
        TextView messageTime = view.findViewById(R.id.messageTime);

        Message message = messages.get(position);

        messageUser.setText(message.getUserName()+":");
        messageText.setText(message.getTextMessage());
        messageTime.setText(message.getMessageTime());
        return view;
    }
}
