package edu.unsw.infs.assignment.textbook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import edu.unsw.infs.assignment.R;

/**
 * Created by June on 6/10/2017.
 *
 *  References:
 *  1. Master Detail Flow sample code from android studio
 *  2. Email intent from INFS3634 Week 06 Just-Java-Master
 */

public class TextBookDetailActivity extends AppCompatActivity {
    private static final String TAG = "TextBookDetailActivity";
    public static Context mContext;

    public static String selectedTopic;

    private TopicModel.TopicItem[] mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.textbook_item_detail_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        mContext = getApplicationContext();

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                switch (choice) {
                    case DialogInterface.BUTTON_POSITIVE:

                        // Use an intent to launch an email app.
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        // only email apps should handle this
                        intent.setData(Uri.parse("mailto:"));
                        intent.putExtra(Intent.EXTRA_SUBJECT, selectedTopic);

                        if (intent.resolveActivity(getPackageManager()) != null) {
                            Log.d(TAG, "User wants to send email");
                            startActivity(intent);
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        Log.d(TAG, "User clicks cancel in the dialogInterface");
                        break;
                }
            }
        };

        //Send email intent about relevant topic on the fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TextBookDetailActivity.this);
                builder.setMessage("Do you want to send an email to LiC about this topic?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                Log.d(TAG, "FAB is clicked, dialogInterface is called.");
            }
        });

         /*savedInstanceState is non-null when there is fragment state saved from previous configurations of this activity
         (e.g. when rotating the screen from portrait to landscape).
         In this case, the fragment will automatically be re-added to its container so we don't need to manually add it.
         For more information, see the Fragments API guide at:

         http://developer.android.com/guide/components/fragments.html*/

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity using a fragment transaction.
            Bundle arguments = getIntent().getExtras();
            selectedTopic = arguments.getString(TextBookListActivity.SELECTED_TOPIC);
            appBarLayout.setTitle(selectedTopic);

            Log.d(TAG, "Set Toolbar text to " + selectedTopic);

            mItem = new TopicModel.TopicItem[3];
            for (int i = 0; i < 3; i++) {
                mItem[i] = new TopicModel.TopicItem().getTopicItemSet(this.getApplicationContext(), Integer.toString(i + 1), selectedTopic);
                Log.d(TAG, "Insert data"+i);
            }

            arguments.putString(TextBookDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(TextBookDetailFragment.ARG_ITEM_ID));
            TextBookDetailFragment fragment = new TextBookDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            appBarLayout.setTitle(selectedTopic);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            /*This ID represents the Home or Up button. In the case of this
             activity, the Up button is shown. Use NavUtils to allow users
             to navigate up one level in the application structure. For
             more details, see the Navigation pattern on Android Design:

             http://developer.android.com/design/patterns/navigation.html#up-vs-back*/

            NavUtils.navigateUpTo(this, new Intent(this, TextBookListActivity.class));
            Log.d(TAG, "Return to TextBookListActivity.class");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.d(TAG, "onRestoreInstanceState");
    }

}
