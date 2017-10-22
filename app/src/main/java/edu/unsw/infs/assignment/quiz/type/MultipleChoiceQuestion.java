package edu.unsw.infs.assignment.quiz.type;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import edu.unsw.infs.assignment.quiz.Marker;
import edu.unsw.infs.assignment.quiz.Question;
import edu.unsw.infs.assignment.quiz.QuestionBuilder;

/**
 * Created by Nodin on 12-Oct-17.
 */

/**
 * Represents a {@link Question} of response type MCQ
 */
public class MultipleChoiceQuestion implements Question {

    public static final Creator<MultipleChoiceQuestion> CREATOR = new Creator<MultipleChoiceQuestion>() {

        @Override
        public MultipleChoiceQuestion createFromParcel(Parcel parcel) {
            return new MultipleChoiceQuestion(parcel);
        }

        @Override
        public MultipleChoiceQuestion[] newArray(int i) {
            return new MultipleChoiceQuestion[0];
        }
    };

    private static final String TYPE = "MCQ";

    private final String context;

    private final Map<String, Boolean> choices;

    private final int weight;

    private MultipleChoiceQuestion(String context, Map<String, Boolean> choices, int weight) {
        this.context = context;
        this.choices = choices;
        this.weight = weight;
    }

    private MultipleChoiceQuestion(Parcel in) {
        this(in.readString(), in.readHashMap(Boolean.class.getClassLoader()), in.readInt());
    }

    @Override
    public View buildInteractableView(Context context) {
        LinearLayout group = new LinearLayout(context);

        group.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        group.setPadding     (6, 6, 6, 6);
        group.setOrientation (LinearLayout.VERTICAL);

        List<Map.Entry<String, Boolean>> choices = new ArrayList<>(getChoices().entrySet());

        Collections.shuffle(choices);

        for (Map.Entry<String, Boolean> choice : choices) {
            CheckBox box = new CheckBox(context);

            box.setPadding(12, 12, 12, 12);
            box.setText   (choice.getKey());
            box.setTextColor((Color.rgb(0, 0, 0)));
            box.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            box.setTag    (choice.getValue());

            group.addView(box);
        }

        return group;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Map<String, Boolean> getChoices() {
        return choices;
    }

    @Override
    public String getContext() {
        return context;
    }

    @Override
    public Marker getMarker() {
        return new Marker() {

            @Override
            public void onSubmit(View interactable, AtomicInteger score) {
                LinearLayout group = LinearLayout.class.cast(interactable);

                group.setEnabled(false);

                boolean correct = true;

                for (int i = 0; i < group.getChildCount(); i++) {
                    CheckBox choice = CheckBox.class.cast(group.getChildAt(i));

                    choice.setEnabled(false);

                    if (choice.isChecked()) {
                        if (!Boolean.class.cast(choice.getTag())) {
                            correct = false;
                            choice.setBackgroundColor(Color.rgb(255, 105, 97));

                        } else {
                            choice.setBackgroundColor(Color.rgb(119, 221, 119));
                        }

                    } else {
                        if (!Boolean.class.cast(choice.getTag()))
                            continue;

                        correct = false;
                        choice.setBackgroundColor(Color.rgb(247,236,114));
                    }
                }

                if (correct)
                    score.addAndGet(getWeight());
            }
        };
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public void restoreInteractableInstance(ViewGroup interactable, Parcelable parcel) {
        Map<String, Boolean> choices = MultipleChoiceQuestionInstance.class.cast(parcel).choices;

        LinearLayout group = LinearLayout.class.cast(interactable.getChildAt(0));

        for (int i = 0; i < group.getChildCount(); i++) {
            CheckBox box = CheckBox.class.cast(group.getChildAt(i));

            box.setChecked(choices.get(box.getText().toString()));
        }
    }

    @Override
    public Parcelable saveInteractableInstance(ViewGroup interactable) {
        Map<String, Boolean> choices = new HashMap<>();

        LinearLayout group = LinearLayout.class.cast(interactable.getChildAt(0));

        for (int i = 0; i < group.getChildCount(); i++) {
            CheckBox box = CheckBox.class.cast(group.getChildAt(i));

            choices.put(box.getText().toString(), box.isChecked());
        }

        return new MultipleChoiceQuestionInstance(choices);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(context);
        out.writeMap(choices);
        out.writeInt(weight);
    }

    public static final class MultipleChoiceQuestionBuilder implements QuestionBuilder<MultipleChoiceQuestion> {

        @Override
        public MultipleChoiceQuestion build(String context, Map<String, Boolean> choices, int weight) {
            return new MultipleChoiceQuestion(context, choices, weight);
        }
    }

    public static final class MultipleChoiceQuestionInstance implements Parcelable {

        public static final Creator<MultipleChoiceQuestionInstance> CREATOR = new Creator<MultipleChoiceQuestionInstance>() {

            @Override
            public MultipleChoiceQuestionInstance createFromParcel(Parcel parcel) {
                return new MultipleChoiceQuestionInstance(parcel);
            }

            @Override
            public MultipleChoiceQuestionInstance[] newArray(int i) {
                return new MultipleChoiceQuestionInstance[0];
            }
        };

        private Map<String, Boolean> choices;

        private MultipleChoiceQuestionInstance(Map<String, Boolean> choices) {
            this.choices = choices;
        }

        private MultipleChoiceQuestionInstance(Parcel in) {
            this(in.readHashMap(Boolean.class.getClassLoader()));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeMap(choices);
        }
    }
}
