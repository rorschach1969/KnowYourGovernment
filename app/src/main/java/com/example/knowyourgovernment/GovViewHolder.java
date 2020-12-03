package com.example.knowyourgovernment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GovViewHolder extends RecyclerView.ViewHolder {
    TextView office;
    TextView nameTitle;

    public GovViewHolder(@NonNull View itemView) {
        super(itemView);
        office = itemView.findViewById(R.id.officeTitleA);
        nameTitle = itemView.findViewById(R.id.nameAndParty);
    }
}
