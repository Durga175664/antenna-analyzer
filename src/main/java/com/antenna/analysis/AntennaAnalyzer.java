package com.antenna.analysis; 

  

import com.antenna.model.AnalysisResult; 

import com.antenna.model.DataPoint; 

  

import java.util.List; 

  

/** Core analysis: resonant frequency, minimum S11, impedance bandwidth, pass/fail. */ 

public final class AntennaAnalyzer { 

  

    private final double thresholdDb; 

  

    /** @param thresholdDb S11 criterion in dB, e.g. -10.0 */ 

    public AntennaAnalyzer(double thresholdDb) { 

        if (thresholdDb >= 0) { 

            throw new IllegalArgumentException("S11 threshold must be negative (dB)"); 

        } 

        this.thresholdDb = thresholdDb; 

    } 

  

    public AnalysisResult analyze(String name, List<DataPoint> points) { 

        if (points == null || points.size() < 3) { 

            throw new IllegalArgumentException("At least 3 data points are required"); 

        } 

        int minIdx = 0; 

        for (int i = 1; i < points.size(); i++) { 

            if (points.get(i).s11Db() < points.get(minIdx).s11Db()) { 

                minIdx = i; 

            } 

        } 

        DataPoint min = points.get(minIdx); 

        boolean meets = min.s11Db() <= thresholdDb; 

  

        double lower = min.frequencyGHz(); 

        double upper = min.frequencyGHz(); 

        if (meets) { 

            lower = findLowerEdge(points, minIdx); 

            upper = findUpperEdge(points, minIdx); 

        } 

        return new AnalysisResult(name, points.size(), min.frequencyGHz(), min.s11Db(), 

                thresholdDb, lower, upper, meets, List.copyOf(points)); 

    } 

  

    private double findLowerEdge(List<DataPoint> p, int minIdx) { 

        int i = minIdx; 

        while (i > 0 && p.get(i - 1).s11Db() <= thresholdDb) { 

            i--; 

        } 

        if (i == 0) { 

            return p.get(0).frequencyGHz();      // band extends beyond measured range 

        } 

        return interpolate(p.get(i - 1), p.get(i)); 

    } 

  

    private double findUpperEdge(List<DataPoint> p, int minIdx) { 

        int i = minIdx; 

        while (i < p.size() - 1 && p.get(i + 1).s11Db() <= thresholdDb) { 

            i++; 

        } 

        if (i == p.size() - 1) { 

            return p.get(i).frequencyGHz(); 

        } 

        return interpolate(p.get(i), p.get(i + 1)); 

    } 

  

    /** Linear interpolation of the frequency at which S11 crosses the threshold. */ 

    private double interpolate(DataPoint a, DataPoint b) { 

        double t = (thresholdDb - a.s11Db()) / (b.s11Db() - a.s11Db()); 

        return a.frequencyGHz() + t * (b.frequencyGHz() - a.frequencyGHz()); 

    } 

} 