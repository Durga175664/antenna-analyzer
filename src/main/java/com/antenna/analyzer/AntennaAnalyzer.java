package com.antenna.analyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AntennaAnalyzer {

    public List<MeasurementPoint> loadData(String filePath) throws IOException {
        List<MeasurementPoint> points = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Header line skip panrom
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                double freq = Double.parseDouble(values[0].trim());
                double s11 = Double.parseDouble(values[1].trim());
                points.add(new MeasurementPoint(freq, s11));
            }
        }
        return points;
    }

    public AnalysisResult analyze(List<MeasurementPoint> points, double s11Threshold, double targetMinS11) {
        if (points.isEmpty()) {
            throw new IllegalArgumentException("Dataset empty-ah irukku!");
        }

        MeasurementPoint minPoint = points.get(0);
        for (MeasurementPoint p : points) {
            if (p.getS11() < minPoint.getS11()) {
                minPoint = p;
            }
        }

        double startFreq = -1;
        double endFreq = -1;
        boolean inBand = false;

        for (MeasurementPoint p : points) {
            if (p.getS11() <= s11Threshold) {
                if (!inBand) {
                    startFreq = p.getFrequency();
                    inBand = true;
                }
                endFreq = p.getFrequency();
            } else if (inBand) {
                break;
            }
        }

        double bandwidth = (startFreq != -1 && endFreq != -1) ? (endFreq - startFreq) : 0.0;
        boolean meetsCriterion = minPoint.getS11() <= targetMinS11;

        return new AnalysisResult(minPoint.getS11(), minPoint.getFrequency(), bandwidth, meetsCriterion);
    }
}