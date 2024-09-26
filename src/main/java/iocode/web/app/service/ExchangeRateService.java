package iocode.web.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * This class is responsible for fetching and storing the latest exchange rates for a set of currencies.
 * It uses the Currency API to retrieve the exchange rates.
 */
@Component
@RequiredArgsConstructor
@Getter
public class ExchangeRateService {

    private final RestTemplate restTemplate;

    /**
     * A map to store the exchange rates for each currency.
     */
    private Map<String, Double> rates = new HashMap<>();

    /**
     * A set of currencies for which exchange rates are fetched.
     */
    private final Set<String> CURRENCIES = Set.of(
        "USD",
        "EUR",
        "GBP",
        "JPY",
        "NGN",
        "INR"
    );

    /**
     * The API key for accessing the Currency API.
     */
    @Value("${currencyApiKey}")
    private String apiKey;

    /**
     * Fetches the latest exchange rates for the specified currencies from the Currency API and stores them in the rates map.
     */
    public void getExchangeRate() {
        String CURRENCY_API = "https://api.currencyapi.com/v3/latest?apikey=";
        var response = restTemplate.getForEntity(CURRENCY_API + apiKey, JsonNode.class);
        var data = Objects.requireNonNull(response.getBody()).get("data");
        for (var currency : CURRENCIES) {
            rates.put(currency, data.get(currency).get("value").doubleValue());
        }
        System.out.println(rates);
    }
}
