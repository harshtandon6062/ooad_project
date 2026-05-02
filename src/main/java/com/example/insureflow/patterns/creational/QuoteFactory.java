package com.example.insureflow.patterns.creational;

import com.example.insureflow.model.Quote;

import java.time.LocalDate;

public class QuoteFactory {

    public static Quote createQuote(double premium, LocalDate validUntil) {

        return new Quote(null, null, premium, validUntil);

    }

    // Additional methods would require repository access

}