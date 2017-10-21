package edu.unsw.infs.assignment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import edu.unsw.infs.assignment.textbook.TextBookListActivity;
import edu.unsw.infs.assignment.quiz.QuizActivity;

/* References:
* 1. font: http://www.charlesdaoud.com/download-dense-regular.html
* */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView madQuiz = (TextView) findViewById(R.id.madQuiz);
        TextView mobile = (TextView) findViewById(R.id.mobileTV);
        TextView application = (TextView) findViewById(R.id.applicationTV);
        TextView development = (TextView) findViewById(R.id.developmentTV);

        Button quizButton = (Button) findViewById(R.id.quizButton);
        Button textbookButton = (Button) findViewById(R.id.textBookButton);
        ImageButton aboutButton = (ImageButton) findViewById(R.id.forthButton);

        //use custom font
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Dense-Regular.otf");
        madQuiz.setTypeface(custom_font);
        mobile.setTypeface(custom_font);
        application.setTypeface(custom_font);
        development.setTypeface(custom_font);

        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), QuizActivity.class);
                startActivity(intent);
            }
        });

        textbookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), TextBookListActivity.class);
                startActivity(intent);
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AboutActivity.class);
                startActivity(intent);

            }
        });
    }
}
