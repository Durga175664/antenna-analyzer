package com.antenna.analyzer;

public class AnalysisResult {
    private double minS11;
    private double resonantFrequency;
    private double bandwidth;
    private boolean meetsCriterion;

    public AnalysisResult(double minS11, double resonantFrequency, double bandwidth, boolean meetsCriterion) {
        this.minS11 = minS11;
        this.resonantFrequency = resonantFrequency;
        this.bandwidth = bandwidth;
        this.meetsCriterion = meetsCriterion;
    }

    public double getMinS11() { return minS11; }
    public double getResonantFrequency() { return resonantFrequency; }
    public double getBandwidth() { return bandwidth; }
    public boolean isMeetsCriterion() { return meetsCriterion; }
}