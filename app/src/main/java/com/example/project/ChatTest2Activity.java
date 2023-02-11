package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChatTest2Activity extends AppCompatActivity {
    DBHelper DB;
    ImageButton back;
    private ArrayList<String> chats = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView chatList;
    private EditText inputChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_test2);
        DB = new DBHelper(this);
        chats = DB.loadChats();

        chatList = (ListView) findViewById(R.id.chat_list);
        inputChat = (EditText) findViewById(R.id.input_chat);
        back = findViewById(R.id.back);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chats);
        chatList.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), aboutusActivity.class);
                startActivity(intent);
            }
        });
    }

    public void sendChat(View view) {
        DB = new DBHelper(this);
        String chat = inputChat.getText().toString();
        if (!chat.isEmpty()) {
            chats.add(chat);
            adapter.notifyDataSetChanged();
            inputChat.setText("");
            boolean insert = DB.insertChat(chat);
            if(insert) {
                Toast.makeText(ChatTest2Activity.this, "Message Sent", Toast.LENGTH_SHORT).show();
            }
        }
    }
}