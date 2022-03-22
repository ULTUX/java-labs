package pl.edu.pwr.quizapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Questions {
    private List<Question> questions;

    public Questions() {
        questions = new ArrayList<>();

        //Question: Give any city that is located in poland.
        questions.add(new Question(LocalizableStrings.Q_FIND_CITY_IN_PL, LocalizableStrings.A_FIND_CITY_IN_PL, answer -> {
            answer = JSONDataParser.urlEncode(answer);
            JSONDataParser cityInfoUrl = new JSONDataParser("https://api.teleport.org/api/cities/?search="+answer,
                    "$['_embedded']['city:search-results'][0]['_links']['city:item']['href']");
            String url = cityInfoUrl.getValue();
            System.out.println(url);
            JSONDataParser owningCountry = new JSONDataParser(url, "$['_links']['city:country']['name']");
            return owningCountry.getValue().equals("Poland");
        }));

        questions.add(new Question(LocalizableStrings.Q_FIND_CITY_IN_GB, LocalizableStrings.A_FIND_CITY_IN_GB, answer -> {
            answer = JSONDataParser.urlEncode(answer);
            JSONDataParser cityInfoUrl = new JSONDataParser("https://api.teleport.org/api/cities/?search="+answer,
                    "$['_embedded']['city:search-results'][0]['_links']['city:item']['href']");
            String url = cityInfoUrl.getValue();
            System.out.println(url);
            JSONDataParser owningCountry = new JSONDataParser(url, "$['_links']['city:country']['name']");
            return owningCountry.getValue().equals("United Kingdom");
        }));


    }

    public Question getRandomQuestion(){
        int randIndex = ThreadLocalRandom.current().nextInt(0, questions.size());
        return questions.get(randIndex);
    }
}
