package edu.unsw.infs.assignment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * Created by June on 15/10/2017.
 */

public class AboutActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        //Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("About");
        setSupportActionBar(toolbar);


        TextView aboutTitle = (TextView) findViewById(R.id.aboutTitle);
        //use custom font
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Dense-Regular.otf");
        aboutTitle.setTypeface(custom_font);
    }
}
