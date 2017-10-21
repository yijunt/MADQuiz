package edu.unsw.infs.assignment.textbook;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import edu.unsw.infs.assignment.R;

import static edu.unsw.infs.assignment.textbook.TextBookListActivity.SELECTED_TOPIC;

/**
 * Created by June on 6/10/2017.
 * <p>
 * References:
 * 1. Master Detail Flow sample code from android studio
 */

public class TextBookDetailFragment extends Fragment {

    public static final String TAG = "TextBookDetailFragment";
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "selected_topic";

    /**
     * The TextBook content this fragment is presenting.
     */
    private TopicModel.TopicItem[] mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TextBookDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItem = new TopicModel.TopicItem[3];
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            //Get TopicItemData
            for (int i = 0; i < 3; i++) {
                mItem[i] = new TopicModel.TopicItem().getTopicItemSet(this.getContext(), Integer.toString(i + 1), getArguments().getString(ARG_ITEM_ID));

                Log.d(TAG, "Insert data" + i);
            }

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(SELECTED_TOPIC);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.textbook_item_detail_content, container, false);

        // Show the TextBook content as text in a TextView.
        TextView text_detail1 = rootView.findViewById(R.id.text_detail1);
        text_detail1.setText(mItem[0].getSubTopic());
        TextView text_detailDesc1 = rootView.findViewById(R.id.item_detailDesc1);
        text_detailDesc1.setText(mItem[0].getDetails());

        TextView text_detail2 = rootView.findViewById(R.id.text_detail2);
        text_detail2.setText(mItem[1].getSubTopic());
        TextView text_detailDesc2 = rootView.findViewById(R.id.item_detailDesc2);
        text_detailDesc2.setText(mItem[1].getDetails());

        TextView text_detail3 = rootView.findViewById(R.id.text_detail3);
        text_detail3.setText(mItem[2].getSubTopic());
        TextView text_detailDesc3 = rootView.findViewById(R.id.item_detailDesc3);
        text_detailDesc3.setText(mItem[2].getDetails());

        Log.d(TAG, "set relevant data to TextView");

        //When user clicks subTopic:
        if (!mItem[0].getLink().equals("")) {
            text_detail1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Convert String URL into a URI object to pass into the Intent constructor
                    Uri link = Uri.parse(mItem[0].getLink());

                    //Create a new Intent to view the URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, link);

                    //send the Intent to launch a new activity
                    startActivity(websiteIntent);

                    Log.d(TAG, "Start website Intent");
                }
            });
        } else {
            returnToast(text_detail1);
        }
        if (!mItem[1].getLink().equals("")) {
            text_detail2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Convert String URL into a URI object to pass into the Intent constructor
                    Uri link = Uri.parse(mItem[1].getLink());

                    //Create a new Intent to view the URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, link);

                    //send the Intent to launch a new activity
                    startActivity(websiteIntent);

                    Log.d(TAG, "Start website Intent");
                }
            });
        } else {
            returnToast(text_detail2);
        }
        if (!mItem[2].getLink().equals("")) {
            text_detail3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Convert String URL into a URI object to pass into the Intent constructor
                    Uri link = Uri.parse(mItem[2].getLink());

                    //Create a new Intent to view the URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, link);

                    //send the Intent to launch a new activity
                    startActivity(websiteIntent);

                    Log.d(TAG, "Start website Intent");
                }
            });
        } else {
            returnToast(text_detail3);
        }

        return rootView;
    }

    //create toast if the topic does not have a link
    public void returnToast(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "This topic does not have a link", Toast.LENGTH_SHORT).show();

                Log.d(TAG, "Start website Intent but no link assigned");
            }
        });
    }

}
