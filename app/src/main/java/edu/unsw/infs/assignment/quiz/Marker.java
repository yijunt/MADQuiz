package edu.unsw.infs.assignment.quiz;

import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Nodin on 12-Oct-17.
 */

public interface Marker {

    void onSubmit(View interactable, AtomicInteger score);
}
