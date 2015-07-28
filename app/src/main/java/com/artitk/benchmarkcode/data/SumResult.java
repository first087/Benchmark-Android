package com.artitk.benchmarkcode.data;

import java.util.ArrayList;

public class SumResult {
    private String round;
    private ArrayList<Long> sumResult;

    public SumResult(String round) {
        this.round = round;
        sumResult = new ArrayList<>();
    }

    public String getRound() {
        return round;
    }

    public ArrayList<Long> getSumResult() {
        return sumResult;
    }
}
