package edu.unsw.infs.assignment.quiz;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Nodin on 12-Oct-17.
 */

public interface Question extends Parcelable {

    View buildInteractableView(Context context);

    String getContext();

    Marker getMarker();

    String getType();

    int getWeight();

    void restoreInteractableInstance(ViewGroup interactable, Parcelable parcel);

    Parcelable saveInteractableInstance(ViewGroup interactable);
}
