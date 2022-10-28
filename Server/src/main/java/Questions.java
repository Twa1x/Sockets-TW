import java.security.PrivateKey;
import java.util.*;
import java.util.Random;
public class Questions {

    private Vector<String> questions = new Vector<String>();

    public Questions() {
        questions.add("What is your first dog name?");
        questions.add("What is your mother name?");
        questions.add("What is your first cat name?");
        questions.add("How old were you in 2000?");
        questions.add("What is your nickname?");
    }

    public void addQuestion(String question)
    {
        questions.add(question);
    }

    public Vector<String> getQuestions()
    {
        return questions;
    }

    public String getRandomQuestion()
    {
        Random rand = new Random();
        int rand_int = rand.nextInt(questions.size());

        return questions.elementAt(rand_int);
    }



}
