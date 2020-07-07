package com.miggens.evidence.greedycoins.models;

import java.util.Map;

public class USCurrencyParsed {

    private int dollars;
    private int halfDollars;
    private int quarters;
    private int dimes;
    private int nickels;
    private int pennies;

    public USCurrencyParsed(Map<String, Integer> values) {
        this.dollars = values.get("dollar");
        this.halfDollars = values.get("halfDollar");
        this.quarters = values.get("quarter");
        this.dimes = values.get("dime");
        this.nickels = values.get("nickel");
        this.pennies = values.get("penny");
    }

    public int getDollars() {
        return dollars;
    }

    public int getHalfDollars() {
        return halfDollars;
    }

    public int getQuarters() {
        return quarters;
    }

    public int getDimes() {
        return dimes;
    }

    public int getNickels() {
        return nickels;
    }

    public int getPennies() {
        return pennies;
    }
}
