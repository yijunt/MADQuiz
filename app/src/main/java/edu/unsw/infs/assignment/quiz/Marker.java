package edu.unsw.infs.assignment.quiz;

import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Nodin on 12-Oct-17.
 */

/**
 * {@link FunctionalInterface} for marking {@link Question}s
 */
public interface Marker {

    /**
     * Called on the submission of a {@link Question}
     *
     * @param interactable The {@link View} containing the answers
     * @param score The score counter
     */
    void onSubmit(View interactable, AtomicInteger score);
}
