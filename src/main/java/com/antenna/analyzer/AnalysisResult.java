package com.antenna.analyzer;

public class AnalysisResult {
    private double resonantFrequency;
    private double minS11;
    private double bandwidth;
    private boolean meetsCriterion;

    public AnalysisResult(double resonantFrequency, double minS11, double bandwidth, boolean meetsCriterion) {
        this.resonantFrequency = resonantFrequency;
        this.minS11 = minS11;
        this.bandwidth = bandwidth;
        this.meetsCriterion = meetsCriterion;
    }

    public double getResonantFrequency() {
        return resonantFrequency;
    }

    public double getMinS11() {
        return minS11;
    }

    public double getBandwidth() {
        return bandwidth;
    }

    public boolean isMeetsCriterion() {
        return meetsCriterion;
    }
}