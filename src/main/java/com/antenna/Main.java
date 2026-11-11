package com.antenna; 

  

import com.antenna.analysis.AntennaAnalyzer; 

import com.antenna.io.CsvDataLoader; 

import com.antenna.model.AnalysisResult; 

import com.antenna.report.HtmlReportGenerator; 

  

import java.io.IOException; 

import java.nio.file.Files; 

import java.nio.file.Path; 

import java.nio.file.Paths; 

import java.util.ArrayList; 

import java.util.List; 

import java.util.stream.Stream; 

  

/** Usage: java -jar antenna-analyzer.jar [datasetDir] [outputDir] [thresholdDb] */ 

public final class Main { 

  

    public static void main(String[] args) throws IOException { 

        Path dataDir = Paths.get(args.length > 0 ? args[0] : "datasets"); 

        Path outDir = Paths.get(args.length > 1 ? args[1] : "target/reports"); 

        double threshold = args.length > 2 ? Double.parseDouble(args[2]) : -10.0; 

  

        AntennaAnalyzer analyzer = new AntennaAnalyzer(threshold); 

        List<AnalysisResult> results = new ArrayList<>(); 

        List<Path> files; 

        try (Stream<Path> s = Files.list(dataDir)) { 

            files = s.filter(f -> f.toString().endsWith(".csv")).sorted().toList(); 

        } 

        for (Path f : files) { 

            String name = f.getFileName().toString().replace(".csv", ""); 

            AnalysisResult r = analyzer.analyze(name, CsvDataLoader.load(f)); 

            results.add(r); 

            System.out.printf("%-22s f0=%.3f GHz  minS11=%.2f dB  BW=%.1f MHz  %s%n", 

                    name, r.resonantFrequencyGHz(), r.minS11Db(), r.bandwidthMHz(), 

                    r.meetsCriterion() ? "PASS" : "FAIL"); 

        } 

        Path report = HtmlReportGenerator.write(results, threshold, outDir); 

        System.out.println("HTML report written to: " + report.toAbsolutePath()); 

    } 

} 