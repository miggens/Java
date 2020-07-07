package com.miggens.evidence.greedycoins.controllers;

import com.miggens.evidence.greedycoins.models.Currency;
import com.miggens.evidence.greedycoins.models.USCurrencyParsed;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UIController {

    public static final String PATH = "";

    @Autowired
    RestTemplate restTemplate;

    public UIController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = "/")
    public String getParseDollarForm(Model model) {

        model.addAttribute("currencyAmountIn", new Currency());

        //System.out.println("Resp is " + resp.getStatusCode().toString());
        if (model.containsAttribute("parsedIntoCoins")) {
            USCurrencyParsed currencyParsed = (USCurrencyParsed) model.getAttribute("parsedIntoCoins");
        }

        return "index";
    }

    @PostMapping(value = "/")
    public String parseDollarsToCoins(Model model,
                                      @ModelAttribute("currencyAmountIn") Currency currencyAmountIn,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            System.out.println("BINDING RESULT HAS ERRORS " + bindingResult.getAllErrors());
            return "error";
        }


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        if (!currencyAmountIn.getDollarAmount().contains(".") ||
                currencyAmountIn.getDollarAmount().equals("") ) {

            model.addAttribute("error", "Bad Request");
            model.addAttribute("status", "400");

            return "error";
        }

        String[] dnc = currencyAmountIn.getDollarAmount().split("\\.");
        System.out.println("Dollars and cents " + Arrays.toString(dnc));
        int dollarLen = dnc[0].length();
        int centsLen = dnc[1].length();

        if (centsLen > 2) {
            model.addAttribute("error", "Bad Request");
            model.addAttribute("status", "400");

            return "error";
        }

        for (int i = 0; i < dollarLen; i++) {
            if (!Character.isDigit(dnc[0].charAt(i))) {
                model.addAttribute("error", "Bad Request");
                model.addAttribute("status", "400");

                return "error";
            }
        }

        for (int i = 0; i < centsLen; i++) {
            if (!Character.isDigit(dnc[1].charAt(i))) {
                model.addAttribute("error", "Bad Request");
                model.addAttribute("status", "400");

                return "error";
            }
        }

        Map<String, String> in = new HashMap<>();

        in.put("dollarAmount", currencyAmountIn.getDollarAmount());

        HttpEntity<Map<String, String>> postDataEntity = new HttpEntity<>(in, headers);
        ParameterizedTypeReference<Map<String, Integer>> typeRef = new ParameterizedTypeReference<Map<String, Integer>>() {};
        ResponseEntity<Map<String, Integer>> resp = restTemplate.exchange("http://localhost:5000/greedy/parse", HttpMethod.POST, postDataEntity ,typeRef);
        Map<String, Integer> coins = resp.getBody();

        USCurrencyParsed uscp = new USCurrencyParsed(coins);

        model.addAttribute("parsedIntoCoins", uscp);

        return "index";
    }
}
