package edu.unsw.infs.assignment.quiz;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Created by Nodin on 12-Oct-17.
 */

public class QuestionQueue implements Queue<Question> {

    private final String topic;

    private final List<Question> questions;

    private final int initial_size;

    private int total_weight;

    private Question current;

    public QuestionQueue(String topic, Collection<Question> questions) {
        this.topic = topic;
        this.questions = new ArrayList<>(questions);
        this.initial_size = questions.size();

        for (Question question : questions)
            total_weight += question.getWeight();
    }

    @Override
    public boolean add(Question question) {
        if (question == null)
            throw new NullPointerException();

        return offer(question);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends Question> collection) {
        return questions.addAll(collection);
    }

    @Override
    public void clear() {
        questions.clear();
    }

    @Override
    public boolean contains(Object object) {
        return questions.contains(object);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return questions.containsAll(collection);
    }

    public Question current() {
        return current;
    }

    @Override
    public Question element() {
        if (questions.isEmpty())
            throw new NoSuchElementException();

        return peek();
    }

    public int getInitialSize() {
        return initial_size;
    }

    public String getTopic() {
        return topic;
    }

    public int getTotalweight() {
        return total_weight;
    }

    @Override
    public boolean isEmpty() {
        return questions.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<Question> iterator() {
        return questions.iterator();
    }

    @Override
    public boolean offer(Question question) {
        return questions.add(question);
    }

    @Override
    public Question peek() {
        if(questions.isEmpty())
            return null;

        Collections.shuffle(questions);

        return questions.get(0);
    }

    @Override
    public Question poll() {
        Question element = peek();

        if (element == null)
            return null;

        remove(setCurrent(element));

        return current;
    }

    @Override
    public Question remove() {
        if (questions.isEmpty())
            throw new NoSuchElementException();

        return poll();
    }

    @Override
    public boolean remove(Object object) {
        return questions.remove(object);
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        return questions.removeAll(collection);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return questions.retainAll(collection);
    }

    public Question setCurrent(Question question) {
        return this.current = question;
    }

    @Override
    public int size() {
        return questions.size();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return questions.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] array) {
        return questions.toArray(array);
    }
}
