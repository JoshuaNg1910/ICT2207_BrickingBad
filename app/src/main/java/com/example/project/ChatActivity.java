package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    ArrayList<Message> messages = new ArrayList<>();
    ArrayAdapter<Message> adapter;
    ListView messageList;
    EditText inputMessage;
    Toolbar toolbar;
    String user, otheruser;
    DBHelper db;
    Calendar calendar = Calendar.getInstance();
    Date date = calendar.getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dateString = dateFormat.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messageList = findViewById(R.id.message_list);
        inputMessage = findViewById(R.id.input_message);
        toolbar = findViewById(R.id.chattingToolbar);
        db = new DBHelper(this);
        otheruser = getIntent().getStringExtra("sender");
        toolbar.setTitle(otheruser);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        user = getSharedPreferences("session", MODE_PRIVATE).getString("username", "");
        messages = db.getAllMessages(user, otheruser);
        adapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1, messages) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                String message = getItem(position).getTimestamp() + "\n" + getItem(position).getSender() + ": " + getItem(position).getMessage();
                ((TextView) view).setText(message);
                return view;
            }
        };
        messageList.setAdapter(adapter);
    }

    public void sendMessage(View view) {
        String message = inputMessage.getText().toString();
        if (!message.isEmpty()) {
            Message chat = new Message(user, otheruser, 1, dateString, message, null);
            db.sendChat(chat);
            messages.add(chat);
            adapter.notifyDataSetChanged();
            inputMessage.setText("");
        }
    }
}
