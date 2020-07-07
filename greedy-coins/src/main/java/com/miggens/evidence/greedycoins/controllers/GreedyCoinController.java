package com.miggens.evidence.greedycoins.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@RestController
@RequestMapping( value = GreedyCoinController.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class GreedyCoinController {

    public static final String PATH = "/greedy";

    @PostMapping(value = "/parse")
    public ResponseEntity<Map<String, Integer>> parseDollarAmount(@RequestBody Map<String, String> data) {

        List<Integer> coins = new ArrayList<>(Arrays.asList(100, 50, 25, 10, 5, 1));
        List<String> types = new ArrayList<>(Arrays.asList("dollar", "halfDollar", "quarter", "dime", "nickel", "penny"));
        int len = coins.size();
        Map<String, Integer> coinMap = new HashMap<>();
        for (int idx = 0; idx < len; idx++) {
            coinMap.put(types.get(idx), coins.get(idx));
        }

        Map<String, Integer> parsedDollarInCoins = new HashMap<>();
        types.forEach( type -> {
            parsedDollarInCoins.put(type, 0);
        });

        String amountToParse = data.get("dollarAmount");
        String[] dollarsAndCents = amountToParse.split("\\.");

        int dollarsToParse = dollarsAndCents[0].equals("") ? 0 : Integer.valueOf(dollarsAndCents[0]);

        if (dollarsToParse < 0) return ResponseEntity.badRequest().body(parsedDollarInCoins);

        int centsToParse = Integer.valueOf(dollarsAndCents[1]);

        parsedDollarInCoins.put("dollar", dollarsToParse);

        int typeIdx = 1;
        while(centsToParse > 0) {

            int amountToReduceBy = coinMap.get( types.get(typeIdx) );
            int diff = centsToParse - amountToReduceBy;

            while(diff > -1) {
                int coinCount = parsedDollarInCoins.get( types.get(typeIdx) );
                parsedDollarInCoins.put(types.get(typeIdx), coinCount+1);
                centsToParse -= amountToReduceBy;
                diff = centsToParse - amountToReduceBy;
            }
            typeIdx++;
        }

        return ResponseEntity.ok(parsedDollarInCoins);
    }
}
