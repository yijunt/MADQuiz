package edu.unsw.infs.assignment.quiz;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Nodin on 12-Oct-17.
 */

/**
 * Represents a question
 */
public interface Question extends Parcelable {

    /**
     * Builds a {@link View} as the interactable portion of a question
     *
     * @param context The {@link Context} of the application
     *
     * @return The {@link View} as the interactable portion of a question
     */
    View buildInteractableView(Context context);

    /**
     * Gets the context of the {@link Question}
     *
     * @return The {@link Question} context
     */
    String getContext();

    /**
     * Gets the marking function
     *
     * @return A {@link Marker} responsible for marking the {@link Question}
     */
    Marker getMarker();

    /**
     * Gets the response type of the {@link Question}
     *
     * @return The response type
     */
    String getType();

    /**
     * Gets the weighting of the {@link Question}
     *
     * @return The weighting
     */
    int getWeight();

    /**
     * Restores the instance of the interactable portion of the {@link Question} from the {@link Parcelable}
     *
     * @param interactable The {@link ViewGroup} containing the interactable portion of the question
     * @param parcel The {@link Parcelable} containing the information of the instance
     */
    void restoreInteractableInstance(ViewGroup interactable, Parcelable parcel);

    /**
     * Saves the instance of the interactable portion of the {@link Question} as a {@link Parcelable}
     *
     * @param interactable The {@link ViewGroup} containing the interactable portion of the question
     *
     * @return The {@link Parcelable} containing the interactable portion of the question
     */
    Parcelable saveInteractableInstance(ViewGroup interactable);
}
