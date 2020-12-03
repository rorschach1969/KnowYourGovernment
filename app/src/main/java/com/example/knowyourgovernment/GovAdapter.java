package com.example.knowyourgovernment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GovAdapter extends RecyclerView.Adapter<GovViewHolder> {

    private ArrayList<Government> govList;
    private MainActivity mainActivity;

    public GovAdapter(ArrayList<Government> list, MainActivity mainActivity){
        this.govList = list;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public GovViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gov_item_view, parent, false);
        itemView.setOnClickListener(mainActivity); //how to open notes with short click
        itemView.setOnLongClickListener(mainActivity); //how to prompt deletion of notes
        return new GovViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GovViewHolder holder, int position) {
        Government gov = govList.get(position);
        holder.office.setText(gov.getOfficeTitle());
        holder.nameTitle.setText(gov.getName() + "(" + gov.getParty() + ")");
    }

    @Override
    public int getItemCount() {
        return govList.size();
    }
}
