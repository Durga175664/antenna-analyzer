package com.antenna.analyzer;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AntennaAnalyzerTest {

    @Test
    public void testAntennaPerformance() throws IOException {
        // Dummy dataset CSV create panrom
        File tempFile = File.createTempFile("antenna_data", ".csv");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("Frequency,S11\n");
            writer.write("2.0,-5.0\n");
            writer.write("2.4,-22.5\n"); // Minimum S11
            writer.write("2.8,-4.0\n");
        }

        AntennaAnalyzer analyzer = new AntennaAnalyzer();
        List<MeasurementPoint> data = analyzer.loadData(tempFile.getAbsolutePath());

        // Target S11 <= -15 dB, Threshold <= -10 dB
        AnalysisResult result = analyzer.analyze(data, -10.0, -15.0);

        assertEquals(2.4, result.getResonantFrequency(), 0.001);
        assertEquals(-22.5, result.getMinS11(), 0.001);
        assertTrue(result.isMeetsCriterion(), "Antenna criterion pass aaganum.");

        // HTML Report Generation for Jenkins
        generateHtmlReport(result);
    }

    private void generateHtmlReport(AnalysisResult result) throws IOException {
        File reportDir = new File("target");
        if (!reportDir.exists()) reportDir.mkdirs();
        
        File htmlReport = new File("target/antenna-analysis-report.html");
        try (FileWriter writer = new FileWriter(htmlReport)) {
            writer.write("<html><body>");
            writer.write("<h1>Antenna Measurement Analysis Report</h1>");
            writer.write("<table border='1'>");
            writer.write("<tr><th>Metric</th><th>Value</th></tr>");
            writer.write("<tr><td>Resonant Frequency</td><td>" + result.getResonantFrequency() + " GHz</td></tr>");
            writer.write("<tr><td>Minimum S11</td><td>" + result.getMinS11() + " dB</td></tr>");
            writer.write("<tr><td>Impedance Bandwidth</td><td>" + result.getBandwidth() + " GHz</td></tr>");
            writer.write("<tr><td>Criterion Met</td><td>" + (result.isMeetsCriterion() ? "PASS" : "FAIL") + "</td></tr>");
            writer.write("</table>");
            writer.write("</body></html>");
        }
    }
}