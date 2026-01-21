package com.upipaytrace.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PdfStatementParser implements StatementParser {

    @Override
    public boolean supports(String filename) {
        return filename != null && filename.endsWith(".pdf");
    }

    @Override
    public List<Map<String, String>> parse(MultipartFile file) {

        List<Map<String, String>> rows = new ArrayList<>();

        try (PDDocument document = PDDocument.load(file.getInputStream())) {

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            String[] lines = text.split("\\r?\\n");

            for (String line : lines) {

                if (!line.toUpperCase().contains("UPI"))
                    continue;

                String cleaned = line.trim().replaceAll("\\s+", " ");
                String[] tokens = cleaned.split(" ");

                if (tokens.length < 3)
                    continue;

                String date = tokens[0];
                String amount = tokens[tokens.length - 1];

                String narration = cleaned
                        .replace(date, "")
                        .replace(amount, "")
                        .trim();

                Map<String, String> row = new HashMap<>();
                row.put("Date", date);
                row.put("Narration", narration);

                if (narration.contains("DR")) {
                    row.put("Debit", amount);
                    row.put("Credit", "");
                } else {
                    row.put("Credit", amount);
                    row.put("Debit", "");
                }

                rows.add(row);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse PDF statement");
        }

        return rows;
    }
}
