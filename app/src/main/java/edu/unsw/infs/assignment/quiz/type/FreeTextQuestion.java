package edu.unsw.infs.assignment.quiz.type;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import edu.unsw.infs.assignment.quiz.Marker;
import edu.unsw.infs.assignment.quiz.Question;
import edu.unsw.infs.assignment.quiz.QuestionBuilder;

/**
 * Created by Nodin on 12-Oct-17.
 */

/**
 * Represents a {@link Question} of response type FREE_TEXT
 */
public class FreeTextQuestion implements Question {

    public static final Creator<FreeTextQuestion> CREATOR = new Creator<FreeTextQuestion>() {

        @Override
        public FreeTextQuestion createFromParcel(Parcel parcel) {
            return new FreeTextQuestion(parcel);
        }

        @Override
        public FreeTextQuestion[] newArray(int i) {
            return new FreeTextQuestion[0];
        }
    };

    private static final String TYPE = "FREE_TEXT";

    private final String context;

    private final Set<String> keywords;

    private final int weight;

    private FreeTextQuestion(String context, Set<String> keywords, int weight) {
        this.context = context;
        this.keywords = keywords;
        this.weight = weight;
    }

    private FreeTextQuestion(Parcel in) {
        this(in.readString(), HashSet.class.cast(in.readSerializable()), in.readInt());
    }

    @Override
    public View buildInteractableView(Context context) {
        EditText free_text = new EditText(context);

        free_text.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        free_text.setPadding(12, 12, 12, 12);
        free_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        free_text.setHint   ("Enter your answer here");
        free_text.setTag    (keywords);

        ShapeDrawable background = new ShapeDrawable(new RectShape());

        background.getPaint().setColor(Color.BLACK);
        background.getPaint().setStyle(Paint.Style.STROKE);
        background.getPaint().setStrokeWidth(3);

        free_text.setBackground(background);

        return free_text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String getContext() {
        return context;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    @Override
    public Marker getMarker() {
        return new Marker() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onSubmit(View interactable, AtomicInteger score) {
                EditText free_text = EditText.class.cast(interactable);

                free_text.setEnabled(false);

                boolean correct = true;

                String text              = free_text.getText().toString().toLowerCase();
                Iterator<String> keyword = getKeywords().iterator();

                while (correct && keyword.hasNext()) {
                    correct = text.contains(keyword.next().toLowerCase());
                }

                ShapeDrawable background = new ShapeDrawable(new RectShape());

                background.getPaint().setColor      ((correct) ? Color.rgb(119, 221, 119) : Color.rgb(255, 105,  97));
                background.getPaint().setStyle      (Paint.Style.STROKE);
                background.getPaint().setStrokeWidth(3);

                free_text.setBackground(background);

                if (correct)
                    score.addAndGet(weight);
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
        EditText.class.cast(interactable.getChildAt(0)).setText(FreeTextQuestionInstance.class.cast(parcel).free_text);
    }

    @Override
    public Parcelable saveInteractableInstance(ViewGroup interactable) {
        return new FreeTextQuestionInstance(EditText.class.cast(interactable.getChildAt(0)).getText().toString());
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(context);
        out.writeSerializable(new HashSet<>(keywords));
        out.writeInt(weight);
    }

    public static final class FreeTextQuestionBuilder implements QuestionBuilder<FreeTextQuestion> {

        @Override
        public FreeTextQuestion build(String context, Map<String, Boolean> choices, int weight) {
            Set<String> keywords = new HashSet<>();

            for (Map.Entry<String, Boolean> choice : choices.entrySet()) {
                if (!choice.getValue())
                    continue;

                keywords.add(choice.getKey());
            }

            return new FreeTextQuestion(context, keywords, weight);
        }
    }

    private static final class FreeTextQuestionInstance implements Parcelable {

        public static final Creator<FreeTextQuestionInstance> CREATOR = new Creator<FreeTextQuestionInstance>() {

            @Override
            public FreeTextQuestionInstance createFromParcel(Parcel parcel) {
                return new FreeTextQuestionInstance(parcel);
            }

            @Override
            public FreeTextQuestionInstance[] newArray(int size) {
                return new FreeTextQuestionInstance[size];
            }
        };

        private final String free_text;

        private FreeTextQuestionInstance(String free_text) {
            this.free_text = free_text;
        }

        private FreeTextQuestionInstance(Parcel in) {
            this(in.readString());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int i) {
            out.writeString(free_text);
        }
    }
}