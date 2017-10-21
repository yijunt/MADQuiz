package edu.unsw.infs.assignment.textbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.unsw.infs.assignment.R;

/**
 * Created by June on 6/10/2017.
 * <p>
 * References:
 * 1. Master Detail Flow sample code from android studio
 */

public class TextBookListActivity extends AppCompatActivity {

    private static final String TAG = "TextBookListActivity";

    private List<TopicModel> topicModelList;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textbook_item_list_activity);

        //Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Textbook");
        setSupportActionBar(toolbar);

        topicModelList = new ArrayList<>();
        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;

        prepareTopicList();
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.item_detail_container) != null) {
             /*The detail container view will be present only in the large-screen layouts (res/values-w900dp).
             If this view is present, then the activity should be in two-pane mode.*/
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        SimpleItemRecyclerViewAdapter adapter = new SimpleItemRecyclerViewAdapter(topicModelList);
        recyclerView.setAdapter(adapter);
    }

    //prepare a list of topic
    private void prepareTopicList() {

        String[] topic = new String[]{"Android Fundamentals", "Designing Interactive Apps",
                "Activities and Intents", "Fragments", "Threads and Parallelism", "SQLite and Android"};

//             add topics
        for (int i = 0; i < topic.length; i++) {
            TopicModel topicModel = new TopicModel("Topic 0" + (i + 1), topic[i]);
            topicModelList.add(topicModel);

            Log.d(TAG, "Add" + topic[i] + "into List<TopicModel>");
        }
    }

    public static final String SELECTED_TOPIC = "selected_topic";

    class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final List<TopicModel> topicModelList;

        SimpleItemRecyclerViewAdapter(List<TopicModel> topicModelList) {
            this.topicModelList = topicModelList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.textbook_item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final TopicModel topicModel = topicModelList.get(position);
            holder.mIdView.setText(topicModelList.get(position).getTitleNum());
            holder.mContentView.setText(topicModelList.get(position).getTitle());

            ViewCompat.setTransitionName(holder.mContentView, topicModel.getTitle());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //If is large screen device, put as two pane, else add as activity between list and details
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(SELECTED_TOPIC, topicModel.getTitle());
                        TextBookDetailFragment fragment = new TextBookDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                        Log.d(TAG, "Set layout as two pane with Topic Detail");


                    } else {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, TextBookDetailActivity.class);
                        intent.putExtra(SELECTED_TOPIC, topicModel.getTitle());

                        Log.d(TAG, "Set layout as two activity with Topic Detail");

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return topicModelList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            final View mView;
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = view.findViewById(R.id.topicNum);
                mContentView = view.findViewById(R.id.topicModel);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
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

