package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    Context context;

    DBHelper db;
    Cursor cursor;

    public ChatAdapter(Context context) {
        this.context = context;
        db = new DBHelper(context);
        String username = context.getSharedPreferences("session", Context.MODE_PRIVATE).getString("username", "");
        cursor = db.getAllUsersData(username);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            int nameIndex = cursor.getColumnIndex("username");
            int dpIndex = cursor.getColumnIndex("profilepic");
            if (nameIndex != -1 && dpIndex != -1){
                String name = cursor.getString(nameIndex);
                byte[] dp = cursor.getBlob(dpIndex);
                Bitmap bitmap = BitmapFactory.decodeByteArray(dp, 0, dp.length);
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("sender", name);
                        context.startActivity(intent);
                    }
                });
                holder.imageView.setImageBitmap(bitmap);
                holder.senderName.setText(name);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        CircleImageView imageView;
        TextView senderName;

        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout =  itemView.findViewById(R.id.chatlistlayout);
            imageView = itemView.findViewById(R.id.profilepic);
            senderName = itemView.findViewById(R.id.sendername);
        }
    }
}

