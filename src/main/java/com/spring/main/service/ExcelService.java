package com.spring.main.service;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.spring.main.util.FileUtil;

import java.io.*;
//import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {
    private static final String FILE_PATH = "employees.xlsx";

    public void saveEmployee(String name, String address, String phone) throws IOException {
        File file = new File(FILE_PATH);
        boolean fileExists = file.exists();
        
        try (Workbook workbook = fileExists ? 
                new XSSFWorkbook(new FileInputStream(file)) : 
                new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(file)) {

            Sheet sheet = fileExists ? workbook.getSheetAt(0) : workbook.createSheet("Employees");

            if (!fileExists) {
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("Name");
                header.createCell(1).setCellValue("Address");
                header.createCell(2).setCellValue("Phone");
            }

            int lastRowNum = sheet.getLastRowNum();
            Row row = sheet.createRow(lastRowNum + 1);
            row.createCell(0).setCellValue(name);
            row.createCell(1).setCellValue(address);
            row.createCell(2).setCellValue(phone);

            workbook.write(fos); 
        } 
    }

    public StreamingResponseBody downloadAsCsv() throws IOException {
        List<String[]> data = FileUtil.readExcel(FILE_PATH);

        return outputStream -> {
            for (String[] line : data) {
                String csvLine = String.join(",", line) + "\n";
                outputStream.write(csvLine.getBytes());
            }
        };
    }
}
