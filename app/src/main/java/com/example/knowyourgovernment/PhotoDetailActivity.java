package com.example.knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends AppCompatActivity {
    private View officeActivity;
    private Government gov;
    private ImageView partyLogo;
    private ImageView officialPic;
    private TextView office;
    private TextView name;
    private View photoDetail;
    private TextView loc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        loc = findViewById(R.id.locationViewB);
        photoDetail = findViewById(R.id.photoDetailActivity);
        officeActivity = findViewById(R.id.officialActivityView);
        partyLogo = findViewById(R.id.partylogoA);
        officialPic = findViewById(R.id.photoUrlA);
        office = findViewById(R.id.officeTitleA);
        name = findViewById(R.id.nameA);
        Intent picIntent = getIntent();
        if (picIntent.hasExtra(OfficialActivity.GOV_PIC)){

            gov = (Government) picIntent.getSerializableExtra(OfficialActivity.GOV_PIC);
            loc.setText(picIntent.getStringExtra(OfficialActivity.PIC_LOCATION));
            office.setText(gov.getOfficeTitle());
            name.setText(gov.getName());
            int color = Color.parseColor("#ff0000ff");
            if (gov.getParty().equals("Republican Party")){
                color = Color.parseColor("#ffcc0000");
                partyLogo.setImageResource(R.drawable.rep_logo);
            }
            else if (gov.getParty().equals("Democratic Party")){

            }
            else {
                partyLogo.setVisibility(View.GONE);
                color = Color.parseColor("#ff000000");
            }
            photoDetail.setBackgroundColor(color);
            loadRemoteImage(gov.getPhotoUrl());
        }
    }

    private void loadRemoteImage(final String imageURL) {
        // Needs gradle  implementation 'com.squareup.picasso:picasso:2.71828'

        Picasso picasso = new Picasso.Builder(this).build();
        picasso.load(imageURL)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(officialPic);
    }



}
