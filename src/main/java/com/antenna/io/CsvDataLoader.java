package com.antenna.io; 

  

import com.antenna.model.DataPoint; 

  

import java.io.IOException; 

import java.nio.file.Files; 

import java.nio.file.Path; 

import java.util.ArrayList; 

import java.util.Comparator; 

import java.util.List; 

  

/** Reads "frequency_GHz,s11_dB" CSV files. Lines starting with # are comments. */ 

public final class CsvDataLoader { 

  

    private CsvDataLoader() { } 

  

    public static List<DataPoint> load(Path file) throws IOException { 

        List<DataPoint> points = new ArrayList<>(); 

        int lineNo = 0; 

        for (String raw : Files.readAllLines(file)) { 

            lineNo++; 

            String line = raw.trim(); 

            if (line.isEmpty() || line.startsWith("#")) { 

                continue; 

            } 

            String[] parts = line.split(","); 

            if (parts.length != 2) { 

                throw new IOException(file.getFileName() + ":" + lineNo + " expected 2 columns"); 

            } 

            try { 

                points.add(new DataPoint(Double.parseDouble(parts[0].trim()), 

                                         Double.parseDouble(parts[1].trim()))); 

            } catch (NumberFormatException e) { 

                if (points.isEmpty()) { 

                    continue; // header row 

                } 

                throw new IOException(file.getFileName() + ":" + lineNo + " is not numeric", e); 

            } 

        } 

        if (points.size() < 3) { 

            throw new IOException(file.getFileName() + " has fewer than 3 data points"); 

        } 

        points.sort(Comparator.comparingDouble(DataPoint::frequencyGHz)); 

        return points; 

    } 

} 