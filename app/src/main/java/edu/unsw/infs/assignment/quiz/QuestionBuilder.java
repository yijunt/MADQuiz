package edu.unsw.infs.assignment.quiz;

import java.util.Map;

/**
 * Created by Nodin on 14-Oct-17.
 */

/**
 * {@link FunctionalInterface} for building {@link Question}s
 *
 * @param <T> The specific Class type of the {@link Question}
 */
public interface QuestionBuilder<T extends Question> {

    /**
     * Builds a {@link Question} of type {@link T} from the context, the choices, and the weighting
     *
     * @param context The context of the question
     * @param choices The {@link Map}<{@link String}, {@link Boolean}> of choices within the question
     * @param weight The weighting of the question
     *
     * @return The {@link Question} of type {@link T}
     */
    T build(String context, Map<String, Boolean> choices, int weight);
}
