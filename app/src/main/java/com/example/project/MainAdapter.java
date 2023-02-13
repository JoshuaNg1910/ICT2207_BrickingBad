package com.example.project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    Activity activity;
    ArrayList<ContactModel> arrayList;

    public MainAdapter(Activity activity, ArrayList<ContactModel> arrayList){
        this.activity = activity;
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactModel model = arrayList.get(position);
        holder.tvName.setText(model.getName());
        holder.tvNumber.setText(model.getNumber());

    }

    @Override
    public int getItemCount() {

        return this.arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvNumber;

        public ViewHolder(View itemView) {
            super(itemView);

            this.tvName = itemView.findViewById(R.id.tv_name);
            this.tvNumber = itemView.findViewById(R.id.tv_number);
        }
    }
}
