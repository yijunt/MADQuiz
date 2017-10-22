package edu.unsw.infs.assignment.quiz;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import edu.unsw.infs.assignment.quiz.type.FreeTextQuestion;
import edu.unsw.infs.assignment.quiz.type.MultipleChoiceQuestion;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import edu.unsw.infs.assignment.R;

/**
 * Activity Class for Quiz
 */
public class QuizActivity extends AppCompatActivity {

    public static final String TAG = "QuizActivity";

    private TextView score;

    private TextView   context;
    private ScrollView interactable;

    private Button skip;
    private Button submit;
    private Button next;

    private TextView remaining;

    private LinearLayout topics;
    private LinearLayout actions;

    private Map<String, QuestionBuilder<? extends Question>> builders;

    private Map<String, Set<Question>> questions;

    private QuestionQueue queue;
    private AtomicInteger score_value;

    /**
     * Gets a {@link View} of type {@link T} from the ID
     *
     * @param type The Class object of the {@link View}
     * @param id The ID of the {@link View}
     * @param <T> The Class type of the {@link View}
     *
     * @return The {@link View} of type {@link T}
     */
    protected <T extends View> T getView(Class<T> type, int id) {
        return type.cast(findViewById(id));
    }

    /**
     * Loads {@link Question}s from the .XLS File
     */
    public void loadQuestions() {
        this.questions = new HashMap<>();

        Workbook workbook = null;

        try {
            workbook = Workbook.getWorkbook(getAssets().open("quiz_data.xls"));

            Sheet sheet = workbook.getSheet(0);

            for (int row = 0; row < sheet.getRows(); row++) {
                Cell[] columns = sheet.getRow(row);

                if (!questions.containsKey(columns[0].getContents()))
                    questions.put(columns[0].getContents(), new HashSet<Question>());

                if (!builders.containsKey(columns[1].getContents()))
                    continue;

                String context               = columns[2].getContents();
                Map<String, Boolean> choices = new HashMap<>();
                int weight                   = Integer.parseInt(columns[5].getContents());

                for (String valid : columns[3].getContents().split("\\|"))
                    choices.put(valid, true);

                for (String invalid : columns[4].getContents().split("\\|"))
                    choices.put(invalid, false);

                questions.get(columns[0].getContents()).add(builders.get(columns[1].getContents()).build(context, choices, weight));
            }

        } catch (Exception exception) {
            Log.e(QuizActivity.TAG, "Unable to load Quiz", exception);

        } finally {
            if (workbook != null)
                workbook.close();
        }

        this.topics = LinearLayout.class.cast(getLayoutInflater().inflate(R.layout.quiz_topic_layout, null));

        topics.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));

        for (final String topic : questions.keySet()) {
            Button button = Button.class.cast(getLayoutInflater().inflate(R.layout.quiz_topic_button, null));

            button.setText           (topic);
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    onQuizStart(topic);
                }
            });

            topics.addView(button);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        this.builders = new HashMap<>();

        builders.put("FREE_TEXT", new FreeTextQuestion.FreeTextQuestionBuilder());
        builders.put("MCQ", new MultipleChoiceQuestion.MultipleChoiceQuestionBuilder());

        loadQuestions();

        this.score = getView(TextView.class, R.id.score);

        this.context      = getView(TextView.class, R.id.context);
        this.interactable = getView(ScrollView.class, R.id.interactable);

        this.skip   = getView(Button.class, R.id.skip);
        this.submit = getView(Button.class, R.id.submit);
        this.next   = getView(Button.class, R.id.next);

        skip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                skipQuestion();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pollQuestion();
            }
        });

        this.remaining = getView(TextView.class, R.id.remaining);

        this.actions = LinearLayout.class.cast(getLayoutInflater().inflate(R.layout.quiz_actions, null));

        actions.getChildAt(0).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onQuizStart(queue.getTopic());
            }
        });
        actions.getChildAt(1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onQuizEnd();
            }
        });

        context.setText(getString(R.string.quiz_welcome));

        interactable.removeAllViews();
        interactable.addView       (topics);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        context.setText(null);

        interactable.removeAllViews();

        this.builders = null;

        this.questions = null;
    }

    /**
     * Called on quiz completion
     */
    private void onQuizComplete() {
        score.setVisibility(View.INVISIBLE);
        score.setText      (null);

        context.setText("You have scored " + score_value.get() + " out of " + queue.getTotalweight());

        interactable.removeAllViews();
        interactable.addView       (actions);

        skip  .setVisibility(View.INVISIBLE);
        submit.setVisibility(View.INVISIBLE);
        next  .setVisibility(View.INVISIBLE);

        submit.setOnClickListener(null);

        remaining.setVisibility(View.INVISIBLE);
        remaining.setText      (null);

        this.score_value = null;
    }

    /**
     * Called on quiz end
     */
    private void onQuizEnd() {
        score.setVisibility(View.INVISIBLE);

        context.setText(getString(R.string.quiz_welcome));

        interactable.removeAllViews();
        interactable.addView       (topics);

        skip  .setVisibility(View.INVISIBLE);
        submit.setVisibility(View.INVISIBLE);
        next  .setVisibility(View.INVISIBLE);

        remaining.setVisibility(View.INVISIBLE);

        this.queue = null;
    }

    /**
     * Called on quiz start
     *
     * @param topic The chosen topic
     */
    private void onQuizStart(String topic) {
        queue        = new QuestionQueue(topic, questions.get(topic));
        score_value  = new AtomicInteger();

        score.setVisibility(View.VISIBLE);

        interactable.removeAllViews();

        pollQuestion();

        skip  .setVisibility(View.VISIBLE);
        submit.setVisibility(View.VISIBLE);
        next  .setVisibility(View.VISIBLE);

        remaining.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRestoreInstanceState(Bundle in) {
        if (in.getBoolean("in_progress")) {
            Set<Question> questions = new HashSet<>();

            for (Parcelable question : in.getParcelableArrayList("questions"))
                questions.add(Question.class.cast(question));

            queue       = new QuestionQueue(in.getString("topic"), questions);
            score_value = new AtomicInteger();

            Question question = Question.class.cast(in.getParcelable("current"));

            queue      .setCurrent   (question);
            score_value.set(in.getInt("score") - question.getWeight());

            score.setVisibility(View.VISIBLE);
            score.setText      (score_value.get() + "/" + queue.getTotalweight());

            interactable.removeAllViews();
            interactable.addView(question.buildInteractableView(getApplicationContext()));

            question.restoreInteractableInstance(interactable, in.getParcelable("interactable"));

            skip  .setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);
            next  .setVisibility(View.VISIBLE);

            submit.setOnClickListener(new SubmitListener(this, question.getMarker()));

            remaining.setVisibility(View.VISIBLE);

            switch (queue.size()) {

                case 0:
                    remaining.setText(getString(R.string.quiz_last_question));
                    break;

                case 1:
                    remaining.setText(getString(R.string.quiz_last_remaining));
                    break;

                default:
                    remaining.setText(String.format(getString(R.string.quiz_remaining_question), queue.size()));
                    break;
            }

            if (in.getBoolean("submitted")) {
                skip  .setEnabled(false);
                submit.setEnabled(false);
                next  .setEnabled(true);

                question.getMarker().onSubmit(interactable.getChildAt(0), score_value);

            } else {
                score_value.set(in.getInt("score"));
                
                skip  .setEnabled(true);
                submit.setEnabled(true);
                next  .setEnabled(false);
            }
        }

        super.onRestoreInstanceState(in);
    }

    @Override
    public void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);

        if (queue != null) {
            out.putBoolean("in_progress", true);

            out.putString("topic", queue.getTopic());

            out.putParcelableArrayList("questions", new ArrayList<>(queue));

            out.putInt("score", score_value.get());

            out.putParcelable("current", queue.current());

            out.putString    ("context", context.getText().toString());
            out.putParcelable("interactable", queue.current().saveInteractableInstance(interactable));

            out.putBoolean("submitted", !submit.isEnabled());

        } else {
            out.putBoolean("in_progress", false);
        }
    }

    /**
     * Called on {@link Question} change
     */
    private void pollQuestion() {
        if (!queue.isEmpty()) {
            Question question = queue.poll();

            score.setText(score_value.get() + "/" + queue.getTotalweight());

            context.setText(question.getContext());

            interactable.removeAllViews();
            interactable.addView       (question.buildInteractableView(getApplicationContext()));

            skip  .setEnabled(true);
            submit.setEnabled(true);
            next  .setEnabled(false);

            submit.setOnClickListener(new SubmitListener(this, question.getMarker()));

            switch (queue.size()) {

                case 0:
                    remaining.setText(getString(R.string.quiz_last_question));
                    break;

                case 1:
                    remaining.setText(getString(R.string.quiz_last_remaining));
                    break;

                default:
                    remaining.setText(String.format(getString(R.string.quiz_remaining_question), queue.size()));
                    break;
            }

        } else {
            onQuizComplete();
        }
    }

    /**
     * Called on {@link Question} skip
     */
    private void skipQuestion() {
        if (!queue.isEmpty()) {
            Question skipped  = queue.current();

            pollQuestion();

            queue.offer(skipped);

            remaining.setText(String.format(getString(R.string.quiz_remaining_question), queue.size()));

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.quiz_skip_warning), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Wrapper for submission {@link Marker}s
     */
    private static final class SubmitListener implements View.OnClickListener {

        private final QuizActivity activity;

        private final Marker marker;

        private SubmitListener(QuizActivity activity, Marker marker) {
            this.activity = activity;
            this.marker = marker;
        }

        @Override
        public void onClick(View view) {
            activity.skip  .setEnabled(false);
            activity.submit.setEnabled(false);
            activity.next  .setEnabled(true);

            marker.onSubmit(activity.interactable.getChildAt(0), activity.score_value);
        }
    }
}
