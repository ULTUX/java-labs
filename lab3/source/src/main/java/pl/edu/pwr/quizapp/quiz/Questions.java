package pl.edu.pwr.quizapp.quiz;

import pl.edu.pwr.quizapp.json.JSONDataParser;
import pl.edu.pwr.quizapp.lang.LocalizableStrings;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Questions {
    private final List<Question> questionList;

    public Questions() {
        questionList = new ArrayList<>();

        //Question: Give any city that is located in poland.
        questionList.add(new Question(LocalizableStrings.Q_FIND_CITY_IN, LocalizableStrings.A_FIND_CITY_IN, answer -> {
            String city = JSONDataParser.urlEncode(answer[0]);
            String country = answer[1];
            JSONDataParser cityInfoUrl = new JSONDataParser("https://api.teleport.org/api/cities/?search=" + city,
                    "$['_embedded']['city:search-results'][0]['_links']['city:item']['href']");
            String url = cityInfoUrl.getValue();
            JSONDataParser owningCountry = new JSONDataParser(url, "$['_links']['city:country']['name']");
            return owningCountry.getValue().equalsIgnoreCase(country);
        }));
        questionList.add(new Question(LocalizableStrings.Q_ALTERNATIVE_NAMES, LocalizableStrings.A_ALTERNATIVE_NAMES, answer -> {
            var city = JSONDataParser.urlEncode(answer[0]);
            var alternativeName = answer[1];
            var cityInfoUrl = new JSONDataParser("https://api.teleport.org/api/cities/?search=" + city,
                    "$['_embedded']['city:search-results'][0]['_links']['city:item']['href']");
            var url = cityInfoUrl.getValue();
            var altNamesP = new JSONDataParser(url + "alternate_names", "$['alternate_names']");
            List<String> alternativeNames = altNamesP.getValueList("name");
            return alternativeNames.stream().anyMatch(s -> s.equalsIgnoreCase(alternativeName));
        }));
        questionList.add(new Question(LocalizableStrings.Q_CURRENCY, LocalizableStrings.A_CURRENCY, answer -> {
            var country = JSONDataParser.urlEncode(answer[0]);
            var currency = answer[1];
            var countryListRequest = new JSONDataParser("https://api.teleport.org/api/countries/",
                    "$['_links']['country:items']");
            List<LinkedHashMap<String, String>> countryMapList = countryListRequest.getMapList();
            var countryDataUrl = countryMapList.stream().filter(stringStringLinkedHashMap ->
                    stringStringLinkedHashMap.containsValue(country)).findAny().orElseThrow().get("href");
            var countryDataRequest = new JSONDataParser(countryDataUrl, "$['currency_code']");
            var currencyCode = countryDataRequest.getValue();
            return currencyCode.equalsIgnoreCase(currency);
        }));

    }

    public Question getRandomQuestion(Question currQuestion) {
        int randIndex;
        do {
            randIndex  = ThreadLocalRandom.current().nextInt(0, questionList.size());
        } while (questionList.get(randIndex).equals(currQuestion));
        Question q = questionList.get(randIndex);
        return q.clone();

    }

}
