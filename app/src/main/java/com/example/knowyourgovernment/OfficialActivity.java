package com.example.knowyourgovernment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OfficialActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{
    private View mainActivity;
    private TextView office;
    private TextView name;
    private TextView party;
    private TextView location;
    private TextView address;
    private TextView phone;
    private TextView urlHeader;
    private TextView phoneHeader;
    private TextView email;
    private TextView url;
    private TextView addressHeader;
    private ImageView partyLogo;
    private ImageView officialPic;
    private int previous;
    private ImageView fb;
    private ImageView gp;
    private ImageView yt;
    private ImageView tw;
    private String facebook;
    private String twitter;
    private String youtube;
    private String googlePlus;
    private View offAct;
    private TextView emailHead;
    private JSONArray channels;
    private Government n;
    public static final String PIC_LOCATION = "PICLOC";
    public static final String GOV_PIC = "GOVPIC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        mainActivity = findViewById(R.id.recyclerView);
        offAct = findViewById(R.id.officialActivityView);
        office = findViewById(R.id.officeTitleA);
        name = findViewById(R.id.nameA);
        officialPic = findViewById(R.id.photoUrlA);
        addressHeader = findViewById(R.id.addressHeader);
        urlHeader = findViewById(R.id.websiteHeader);
        phoneHeader = findViewById(R.id.phoneHeader);
        party = findViewById(R.id.partyUrl);
        partyLogo = findViewById(R.id.partylogoA);
        location = findViewById(R.id.locationViewB);
        address = findViewById(R.id.addressContent);
        emailHead = findViewById(R.id.emailHeader);
        email = findViewById(R.id.emailContent);
        url = findViewById(R.id.urlContent);
        phone = findViewById(R.id.phoneContent);
        fb = findViewById(R.id.facebook);
        tw = findViewById(R.id.twitter);
        yt = findViewById(R.id.youtube);
        gp = findViewById(R.id.googlePlus);
        tw.setVisibility(View.INVISIBLE);
        fb.setVisibility(View.INVISIBLE);
        yt.setVisibility(View.INVISIBLE);
        gp.setVisibility(View.INVISIBLE);
        Intent editIntent = getIntent();
        if (editIntent.hasExtra(MainActivity.GOV_OFFICIAL)){

            n = (Government) editIntent.getSerializableExtra(MainActivity.GOV_OFFICIAL);
            ConstraintSet set = new ConstraintSet();
            ConstraintLayout layout;

            layout = (ConstraintLayout) findViewById(R.id.officialActivityView);
            set.clone(layout);
//            // The following breaks the connection.
//
//            // Comment out line above and uncomment line below to make the connection.
//            set.connect(R.id.bottomText, ConstraintSet.TOP, R.id.imageView, ConstraintSet.BOTTOM, 0);
//            set.applyTo(layout);
            location.setText(editIntent.getStringExtra(MainActivity.LOCATION));
            office.setText(n.getOfficeTitle());
            name.setText(n.getName());
            party.setText(n.getParty());
//            if (n.getParty().equals("")){
//                party.setVisibility(View.GONE);
//                set.clear(R.id.partylogo, ConstraintSet.TOP);
//                previous = R.id.photoUrl;
//            }
//            else{
////                previous = findViewById(R.id.partylogo);
//            }
            int color = Color.parseColor("#ff0000ff");
            previous = R.id.partylogoA;
            if (n.getParty().equals("Republican Party")){
                color = Color.parseColor("#ffcc0000");
                partyLogo.setImageResource(R.drawable.rep_logo);
            }
            else if (n.getParty().equals("Democratic Party")){

            }
            else {
                set.setVisibility(R.id.partylogoA, ConstraintSet.GONE);
                set.applyTo(layout);
                color = Color.parseColor("#ff000000");
                previous = R.id.photoUrlA;
            }
            offAct.setBackgroundColor(color);
            String x = n.getStreetAddress();
            SpannableString content = new SpannableString(n.getStreetAddress() + "\n" + n.getCity() + ", " + n.getState() + " " + n.getZip());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            address.setText(content);
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                previous = R.id.nameA;
            }
            if (n.getStreetAddress().length() < 5){
                set.setVisibility(R.id.addressHeader, ConstraintSet.GONE);
                set.setVisibility(R.id.addressContent, ConstraintSet.GONE);
                set.applyTo(layout);
            }
            else{

                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    previous = R.id.nameA;
                    set.clear(R.id.addressHeader, ConstraintSet.TOP);
                    set.connect(R.id.addressHeader, ConstraintSet.TOP, previous, ConstraintSet.BOTTOM, 48);
                    set.applyTo(layout);
                    previous = R.id.addressContent;
                }
                else {
                    set.clear(R.id.addressHeader, ConstraintSet.TOP);
                    set.connect(R.id.addressHeader, ConstraintSet.TOP, previous, ConstraintSet.BOTTOM, 48);
                    set.applyTo(layout);
                    previous = R.id.addressContent;
                }

            }
            content = new SpannableString(n.getPhoneNum());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            phone.setText(content);
            if (n.getPhoneNum().equals("")){
                set.setVisibility(R.id.phoneHeader, ConstraintSet.GONE);
                set.setVisibility(R.id.phoneContent, ConstraintSet.GONE);
                set.applyTo(layout);
            }
            else{
                set.clear(R.id.phoneHeader, ConstraintSet.TOP);
                set.connect(R.id.phoneHeader, ConstraintSet.TOP, previous, ConstraintSet.BOTTOM, 48);
                set.applyTo(layout);
                previous = R.id.phoneHeader;
            }
            content = new SpannableString(n.getUrl());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            url.setText(content);
            if (n.getUrl().equals("")){
                set.setVisibility(R.id.websiteHeader, ConstraintSet.GONE);
                set.setVisibility(R.id.urlContent, ConstraintSet.GONE);
                set.applyTo(layout);

            }
            else{
                set.clear(R.id.websiteHeader, ConstraintSet.TOP);
                set.connect(R.id.websiteHeader, ConstraintSet.TOP, previous, ConstraintSet.BOTTOM, 48);
                set.applyTo(layout);
                previous = R.id.urlContent;
            }
            content = new SpannableString(n.getEmail());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            email.setText(content);
            if (n.getEmail().equals("")){
                set.setVisibility(R.id.emailHeader, ConstraintSet.GONE);
                set.setVisibility(R.id.emailContent, ConstraintSet.GONE);
                set.applyTo(layout);
            }
            else{
                set.clear(R.id.emailHeader, ConstraintSet.TOP);
                set.connect(R.id.emailHeader, ConstraintSet.TOP, previous, ConstraintSet.BOTTOM, 48);
                set.applyTo(layout);
                previous = R.id.emailContent;
            }

            if (n.getPhotoUrl().equals("")) officialPic.setImageResource(R.drawable.missing);
            else loadRemoteImage(n.getPhotoUrl());
            try {
                channels = new JSONArray(n.getChannels());
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            set.clear(R.id.facebook, ConstraintSet.TOP);
//            set.connect(R.id.facebook, ConstraintSet.TOP, previous, ConstraintSet.BOTTOM, );
//            set.applyTo(layout);
            for (int i = 0; i<channels.length(); i++){
                try {
                    JSONObject chan = channels.getJSONObject(i);
                    switch (chan.getString("type")) {
                        case "GooglePlus":
                            googlePlus = chan.getString("id");
                            gp.setVisibility(View.VISIBLE);
                        case "Facebook":
                            facebook = chan.getString("id");
                            fb.setVisibility(View.VISIBLE);
                        case "Twitter":
                            twitter = chan.getString("id");
                            tw.setVisibility(View.VISIBLE);
                        case "YouTube":
                            youtube = chan.getString("id");
                            yt.setVisibility(View.VISIBLE);
                        default:
                            return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public void twitterClicked(View v) {
        Intent intent = null;
        String name = twitter;
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }


    public void facebookClicked(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/" + facebook;
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + facebook;
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }

    public void googlePlusClicked(View v) {
        String name = googlePlus;
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://plus.google.com/" + name)));
        }
    }

    public void youTubeClicked(View v) {
        String name = youtube;
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + name)));
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

    public void clickEmail(View v) {

        Intent intent = new Intent(Intent.ACTION_SENDTO,
                Uri.parse("mailto:"));

        intent.putExtra(Intent.EXTRA_EMAIL, n.getEmail());
        intent.putExtra(Intent.EXTRA_SUBJECT,
                "This comes from EXTRA_SUBJECT");
        intent.putExtra(Intent.EXTRA_TEXT,
                "Email text body from EXTRA_TEXT...");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles SENDTO (mailto) intents");
        }
    }

    public void pictureClicked(View v) {
        if (n.getPhotoUrl().equals("")){
            return;
        }
        else {
            Intent selectedGov = new Intent(this, PhotoDetailActivity.class);
            selectedGov.putExtra(GOV_PIC, n);
            selectedGov.putExtra(PIC_LOCATION, location.getText().toString());
            startActivity(selectedGov);
        }
    }

    private void makeErrorAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle("No App Found");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void clickCall(View v) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + n.getPhoneNum()));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_DIAL (tel) intents");
        }
    }

    public void clickMap(View v) {

        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address.getText().toString()));

        Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (geo) intents");
        }
    }

    public void clickURL(View v) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(n.getUrl()));
        startActivity(i);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
