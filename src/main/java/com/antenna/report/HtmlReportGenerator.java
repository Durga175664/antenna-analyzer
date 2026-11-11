package com.antenna.report; 

  

import com.antenna.model.AnalysisResult; 

import com.antenna.model.DataPoint; 

  

import java.io.IOException; 

import java.nio.file.Files; 

import java.nio.file.Path; 

import java.util.List; 

import java.util.Locale; 

  

/** Produces a self-contained HTML report (summary table + inline SVG S11 plots). */ 

public final class HtmlReportGenerator { 

  

    private HtmlReportGenerator() { } 

  

    public static Path write(List<AnalysisResult> results, double thresholdDb, Path outDir) 

            throws IOException { 

        Files.createDirectories(outDir); 

        Path file = outDir.resolve("antenna-analysis-report.html"); 

        Files.writeString(file, render(results, thresholdDb)); 

        return file; 

    } 

  

    static String render(List<AnalysisResult> results, double thresholdDb) { 

        long passed = results.stream().filter(AnalysisResult::meetsCriterion).count(); 

        StringBuilder h = new StringBuilder(); 

        h.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\">") 

         .append("<title>Antenna Measurement Analysis Report</title><style>") 

         .append("body{font-family:\"Times New Roman\",serif;margin:30px}") 

         .append("table{border-collapse:collapse;width:100%}") 

         .append("th,td{border:1px solid #444;padding:6px;text-align:center}") 

         .append("th{background:#dde6f3}.pass{color:#0a7a0a;font-weight:bold}") 

         .append(".fail{color:#b00020;font-weight:bold}") 

         .append("</style></head><body>"); 

        h.append("<h1>Antenna Measurement Data Analysis Report</h1>"); 

        h.append(String.format(Locale.US, 

                "<p>S11 criterion: <b>S11 &le; %.1f dB</b> &nbsp;|&nbsp; Datasets analysed: <b>%d</b>" 

                + " &nbsp;|&nbsp; Passed: <b>%d</b> &nbsp;|&nbsp; Failed: <b>%d</b></p>", 

                thresholdDb, results.size(), passed, results.size() - passed)); 

        h.append("<table><tr><th>Dataset</th><th>Samples</th><th>Resonant f (GHz)</th>") 

         .append("<th>Min S11 (dB)</th><th>Band edges (GHz)</th><th>Bandwidth (MHz)</th>") 

         .append("<th>Fractional BW (%)</th><th>Verdict</th></tr>"); 

        for (AnalysisResult r : results) { 

            h.append(String.format(Locale.US, 

                    "<tr><td>%s</td><td>%d</td><td>%.3f</td><td>%.2f</td><td>%.3f &ndash; %.3f</td>" 

                    + "<td>%.1f</td><td>%.2f</td><td class=\"%s\">%s</td></tr>", 

                    r.datasetName(), r.sampleCount(), r.resonantFrequencyGHz(), r.minS11Db(), 

                    r.bandwidthLowerGHz(), r.bandwidthUpperGHz(), r.bandwidthMHz(), 

                    r.fractionalBandwidthPercent(), 

                    r.meetsCriterion() ? "pass" : "fail", r.meetsCriterion() ? "PASS" : "FAIL")); 

        } 

        h.append("</table>"); 

        for (AnalysisResult r : results) { 

            h.append("<h2>").append(r.datasetName()).append("</h2>").append(svgPlot(r)); 

        } 

        h.append("</body></html>"); 

        return h.toString(); 

    } 

  

    private static String svgPlot(AnalysisResult r) { 

        int w = 640, hgt = 300, m = 40; 

        List<DataPoint> p = r.points(); 

        double fMin = p.get(0).frequencyGHz(), fMax = p.get(p.size() - 1).frequencyGHz(); 

        double sMin = p.stream().mapToDouble(DataPoint::s11Db).min().orElse(-30) - 2; 

        double sMax = 0; 

        StringBuilder pts = new StringBuilder(); 

        for (DataPoint d : p) { 

            double x = m + (d.frequencyGHz() - fMin) / (fMax - fMin) * (w - 2 * m); 

            double y = m + (d.s11Db() - sMax) / (sMin - sMax) * (hgt - 2 * m); 

            pts.append(String.format(Locale.US, "%.1f,%.1f ", x, y)); 

        } 

        double yT = m + (r.thresholdDb() - sMax) / (sMin - sMax) * (hgt - 2 * m); 

        return String.format(Locale.US, 

                "<svg width=\"%d\" height=\"%d\" style=\"border:1px solid #999\">" 

                + "<polyline fill=\"none\" stroke=\"#1f4e9c\" stroke-width=\"2\" points=\"%s\"/>" 

                + "<line x1=\"%d\" x2=\"%d\" y1=\"%.1f\" y2=\"%.1f\" stroke=\"red\" stroke-dasharray=\"5\"/>" 

                + "<text x=\"%d\" y=\"%d\" font-size=\"12\">%.2f GHz</text>" 

                + "<text x=\"%d\" y=\"%d\" font-size=\"12\">%.2f GHz</text>" 

                + "<text x=\"%d\" y=\"20\" font-size=\"13\">S11 (dB) vs Frequency (GHz); " 

                + "red line = %.1f dB</text></svg>", 

                w, hgt, pts, m, w - m, yT, yT, m, hgt - 10, fMin, w - m - 60, hgt - 10, fMax, 

                m, r.thresholdDb()); 

    } 

} 