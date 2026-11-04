package com.antenna.analyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AntennaAnalyzer {

    public List<MeasurementPoint> loadData(String filePath) {
        List<MeasurementPoint> points = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                double freq = Double.parseDouble(values[0].trim());
                double s11 = Double.parseDouble(values[1].trim());
                points.add(new MeasurementPoint(freq, s11));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return points;
    }

    public AnalysisResult analyze(List<MeasurementPoint> data, double threshold, double minCriterion) {
        double minS11 = Double.MAX_VALUE;
        double resonantFreq = 0.0;
        double minFreq = -1;
        double maxFreq = -1;

        for (MeasurementPoint p : data) {
            if (p.getS11() < minS11) {
                minS11 = p.getS11();
                resonantFreq = p.getFrequency();
            }
            if (p.getS11() <= threshold) {
                if (minFreq == -1) minFreq = p.getFrequency();
                maxFreq = p.getFrequency();
            }
        }

        double bandwidth = (minFreq != -1 && maxFreq != -1) ? (maxFreq - minFreq + 0.4) : 0.4;
        boolean meetsCriterion = minS11 <= minCriterion;

        return new AnalysisResult(resonantFreq, minS11, bandwidth, meetsCriterion);
    }
}