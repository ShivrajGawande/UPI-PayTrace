package com.upipaytrace.parser;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface StatementParser {

    boolean supports(String filename);
    List<Map<String, String>> parse(MultipartFile file);
}
