package hr.algebra.trivialpursuit.trivialpursuit.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class QuestionRepository {

    Map<String, String> questions = new HashMap<>();
    Random randGenerator = new Random();

    public QuestionRepository() {

        questions.put("What is the capital of Germany?", "Berlin");
        questions.put("What is the capital of Greece?", "Athens");
        questions.put("What is the capital of Croatia?", "Zagreb");
        questions.put("What is the capital of The UK?", "London");
        questions.put("What is the name of our planet?", "Earth");
        questions.put("What is the name of our moon?", "Moon");
        questions.put("How many days are in a week?", "7");
        questions.put("How many bones are in a human body?", "206");
    }

    public String getRandomQuestion() {

        int index = randGenerator.nextInt(questions.size() - 1);
        return (String) questions.keySet().toArray()[index];
    }

    public Boolean isAnswerCorrect(final String question, final String playerAnswer) {
        String correctAnswer = questions.get(question);
        return Objects.equals(correctAnswer, playerAnswer);
    }
}
