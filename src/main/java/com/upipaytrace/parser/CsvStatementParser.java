package com.upipaytrace.parser;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.upipaytrace.util.CsvParserUtil;

@Component
public class CsvStatementParser implements StatementParser {

    @Override
    public boolean supports(String filename) {
        return filename != null && filename.endsWith(".csv");
    }

    @Override
    public List<Map<String, String>> parse(MultipartFile file) {
        return CsvParserUtil.parse(file);
    }
}
