package com.example.knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    private TextView apiTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        apiTitle = findViewById(R.id.apiTitle);
        SpannableString content = new SpannableString("Google Civic Information API");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        apiTitle.setText(content);
    }

    public void googleAPI(View v) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        String url = "https://developers.google.com/civic-information/";
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
