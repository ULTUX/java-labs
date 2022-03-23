package pl.edu.pwr.quizapp.quiz;

import pl.edu.pwr.quizapp.json.JSONDataParser;
import pl.edu.pwr.quizapp.lang.LocalizableStrings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Questions {
    private List<Question> questions;

    public Questions() {
        questions = new ArrayList<>();

        //Question: Give any city that is located in poland.
        questions.add(new Question(LocalizableStrings.Q_FIND_CITY_IN, LocalizableStrings.A_FIND_CITY_IN, answer -> {
            String city = JSONDataParser.urlEncode(answer[0]);
            String country = answer[1];
            JSONDataParser cityInfoUrl = new JSONDataParser("https://api.teleport.org/api/cities/?search="+city,
                    "$['_embedded']['city:search-results'][0]['_links']['city:item']['href']");
            String url = cityInfoUrl.getValue();
            JSONDataParser owningCountry = new JSONDataParser(url, "$['_links']['city:country']['name']");
            return owningCountry.getValue().equals(country);
        }));
        questions.add(new Question(LocalizableStrings.Q_ALTERNATIVE_NAMES, LocalizableStrings.A_ALTERNATIVE_NAMES, answer -> {
            String city = JSONDataParser.urlEncode(answer[0]);
            String alternativeName = answer[1];
            JSONDataParser cityInfoUrl = new JSONDataParser("https://api.teleport.org/api/cities/?search="+city,
                    "$['_embedded']['city:search-results'][0]['_links']['city:item']['href']");
            String url = cityInfoUrl.getValue();
            JSONDataParser altNamesP = new JSONDataParser(url+"alternate_names", "$['alternate_names']");
            List<String> alternativeNames = altNamesP.getDataList("name");
            return alternativeNames.stream().anyMatch(s -> s.equals(alternativeName));
        }));


    }

    public Question getRandomQuestion(){
        int randIndex = ThreadLocalRandom.current().nextInt(0, questions.size());
        Question q = questions.get(randIndex);
        return q.clone();

    }

}
