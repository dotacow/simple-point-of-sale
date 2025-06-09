package utils;

import controllers.StatisticsController.SaleSummary;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExporter {

    public static int exportSaleSummaries(List<SaleSummary> summaries, File file) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
        writer.write("Date,Total Sales,Total Revenue\n");
        for (SaleSummary summary : summaries) {
            writer.write(summary.getDate() + "," +
                         summary.getTotalSales() + "," +
                         String.format("%.2f", summary.getTotalRevenue()) + "\n");
        }
        return 0;
    } catch (IOException e) {
        System.err.println("CSV Export Failed: " + e.getMessage());
        return -1;
    }
}
}
