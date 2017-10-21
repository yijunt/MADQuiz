package edu.unsw.infs.assignment.quiz;

import java.util.Map;

/**
 * Created by Nodin on 14-Oct-17.
 */

public interface QuestionBuilder<T extends Question> {

    T build(String context, Map<String, Boolean> choices, int weight);
}
