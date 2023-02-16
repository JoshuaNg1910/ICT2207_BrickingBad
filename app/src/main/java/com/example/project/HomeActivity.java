package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.media.MediaRecorder;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.pm.PackageManager;
import android.widget.Toast;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    Button signup, signin;
    Toolbar toolbar;
    TextView loginRegister;

    ArrayList<ContactModel> arrayList = new ArrayList<>();
    DBHelper DB;
    
    private static final int AUDIO_RECORD_PERMISSION_REQUEST_CODE = 1;
    private static final int c = 180000; //3 minutes

    private MediaRecorder mediaRecorder;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        signup = findViewById(R.id.btnsignup);
        signin = findViewById(R.id.btnsignin);
        toolbar = findViewById(R.id.loginRegisterToolbar);
        loginRegister = findViewById(R.id.loginRegister);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginRegisterActivity.class);
                startActivity(intent);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        
        mHandler = new Handler();

        checkPermission();
    }

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.RECORD_AUDIO}, AUDIO_RECORD_PERMISSION_REQUEST_CODE);
        }
        else {
            startRecording();
            getContactList();
        }
    }

//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == AUDIO_RECORD_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, start recording
//                startRecording();
//            } else {
//                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    @SuppressLint("Range")
    private void getContactList() {
        int counter = 0;
        DB = new DBHelper(this);
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, sort);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";
                Cursor phoneCursor = getContentResolver().query(uriPhone, null, selection, new String[]{id}, null);
                if (phoneCursor.moveToNext()) {
                    String number = phoneCursor.getString(phoneCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    ));
                    ContactModel model = new ContactModel();
                    model.setName(name);
                    model.setNumber(number);
                    arrayList.add(model);

                    File file = new File("/data/data/com.example.project/", "Contacts.txt");
                    try {
                        FileWriter writer = new FileWriter(file, false);
                        writer.append(name + ", " + number + "\n");
                        writer.flush();
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    phoneCursor.close();
                }
            }
            cursor.close();
        }
    }

    public void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        // Generate unique filename for each recording
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "test.mp4";
        String filePath = "/data/data/com.example.project/" + fileName;

        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(filePath);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set maximum duration to unlimited
        mediaRecorder.setMaxDuration(-1);

        mediaRecorder.start();
    }


    public void stopRecording() {
        // Stop recording and release resources
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
