package com.beancrunch.rowfitt.gcp;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class FuzzyExperiment {

    public static void main(String[] args) {
        System.out.println("Ratio :: View Details :: " + FuzzySearch.ratio("View Details", "View Details"));
        System.out.println("Partial ratio :: View Details :: " + FuzzySearch.partialRatio("View Details", "View Details"));

        System.out.println("Ratio :: ew details :: " + FuzzySearch.ratio("ew details", "View Details"));
        System.out.println("Partial ratio :: ew details :: " + FuzzySearch.partialRatio("ew details", "View Details"));

        System.out.println("Ratio :: some crap :: " + FuzzySearch.ratio("some crap", "View Details"));
        System.out.println("Partial ratio :: some crap :: " + FuzzySearch.partialRatio("some crap", "View Details"));

        System.out.println("Ratio :: detai :: " + FuzzySearch.ratio("detail", "View Details"));
        System.out.println("Partial ratio :: Detail :: " + FuzzySearch.partialRatio("Detail", "View Details"));
    }
}
