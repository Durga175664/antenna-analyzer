package com.antenna.model; 

  

import java.util.List; 

  

/** Immutable result of analysing one antenna dataset. */ 

public record AnalysisResult( 

        String datasetName, 

        int sampleCount, 

        double resonantFrequencyGHz, 

        double minS11Db, 

        double thresholdDb, 

        double bandwidthLowerGHz, 

        double bandwidthUpperGHz, 

        boolean meetsCriterion, 

        List<DataPoint> points) { 

  

    /** Impedance bandwidth in MHz (0 when S11 never reaches the threshold). */ 

    public double bandwidthMHz() { 

        return (bandwidthUpperGHz - bandwidthLowerGHz) * 1000.0; 

    } 

  

    /** Fractional bandwidth in percent relative to the resonant frequency. */ 

    public double fractionalBandwidthPercent() { 

        return bandwidthMHz() / (resonantFrequencyGHz * 1000.0) * 100.0; 

    } 

} 