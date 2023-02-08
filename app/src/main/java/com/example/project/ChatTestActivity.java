package com.example.project;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import java.util.ArrayList;

public class ChatTestActivity extends AppCompatActivity {

    private ArrayList<String> messages = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView messageList;
    private EditText inputMessage;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DB = new DBHelper(this);
        setContentView(R.layout.activity_chat_test);
        messages = DB.loadMessages();

        messageList = (ListView) findViewById(R.id.message_list);
        inputMessage = (EditText) findViewById(R.id.input_message);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        messageList.setAdapter(adapter);
    }

    public void sendMessage(View view) {
        DB = new DBHelper(this);

        String message = inputMessage.getText().toString();
        if (!message.isEmpty()) {
            messages.add(message);
            adapter.notifyDataSetChanged();
            inputMessage.setText("");
            Boolean insert = DB.insertMessage(message);
            if(insert) {
                Toast.makeText(ChatTestActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
