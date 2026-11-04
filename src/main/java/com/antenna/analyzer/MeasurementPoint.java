package com.antenna.analyzer;

public class MeasurementPoint {
    private double frequency;
    private double s11;

    public MeasurementPoint(double frequency, double s11) {
        this.frequency = frequency;
        this.s11 = s11;
    }

    public double getFrequency() {
        return frequency;
    }

    public double getS11() {
        return s11;
    }
}