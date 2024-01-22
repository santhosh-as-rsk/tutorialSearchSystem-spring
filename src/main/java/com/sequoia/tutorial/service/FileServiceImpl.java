package com.sequoia.tutorial.service;

import com.sequoia.tutorial.models.ResponseData;
import com.sequoia.tutorial.models.TutorialModel;
import com.sequoia.tutorial.repository.TutorialRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    TutorialRepository tutorialRepository;

    @Override
    public void getExcelSheet(HttpServletResponse response) throws IOException {
        ResponseData responseData = new ResponseData();
//        try {
            List<TutorialModel> tutorialList = tutorialRepository.findAll();
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("tutorial links");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Id");
            header.createCell(1).setCellValue("Topics");
            header.createCell(2).setCellValue("Sub Topics");
            header.createCell(3).setCellValue("Links");
            header.createCell(4).setCellValue("Source");

            int rowNum = 1;
            for (TutorialModel tutorialModel : tutorialList) {
                Row row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(rowNum++);
                row.createCell(1).setCellValue(tutorialModel.getSubTopicsId().getTopicsID().getName());
                row.createCell(2).setCellValue(tutorialModel.getSubTopicsId().getName());
                row.createCell(3).setCellValue(tutorialModel.getLinks());
                row.createCell(4).setCellValue(tutorialModel.getSourceId().getName());
            }

//            response.setHeader("Content-Disposition", "attachment; filename=tutorialLink.xlsx");
//            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            ServletOutputStream outputStream =  response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();


//            return response;
//        } catch (Exception e){
//            responseData.setStatusCode(RESPONSE_CODE_FAILURE);
//            responseData.setMessage(RESPONSE_MESSAGE_FAILURE);
//            responseData.setOutputData(e);
//            return response;
//        }
    }

    @Override
    public byte[] getAllTutorialsAsExcel() {
        List<TutorialModel> tutorials = tutorialRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Tutorials");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Topics");
            headerRow.createCell(1).setCellValue("Links");
            headerRow.createCell(2).setCellValue("Source");
            headerRow.createCell(3).setCellValue("SubTopics ID");
            int rowNum = 1;
            for (TutorialModel tutorial : tutorials) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(tutorial.getSubTopicsId().getTopicsID().getName());
                row.createCell(1).setCellValue(tutorial.getLinks());
                row.createCell(2).setCellValue(tutorial.getSourceId().getName());
                row.createCell(3).setCellValue(tutorial.getSubTopicsId().getName());
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] getTutorialsAsExcel(List<TutorialModel> tutorials) {

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Tutorials");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Topics");
            headerRow.createCell(1).setCellValue("SubTopics");
            headerRow.createCell(2).setCellValue("Links");
            headerRow.createCell(3).setCellValue("Source");
            int rowNum = 1;
            for (TutorialModel tutorial : tutorials) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(tutorial.getSubTopicsId().getTopicsID().getName());
                row.createCell(1).setCellValue(tutorial.getSubTopicsId().getName());
                row.createCell(2).setCellValue(tutorial.getLinks());
                row.createCell(3).setCellValue(tutorial.getSourceId().getName());
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public byte[] getTutorialsAsCsv(List<TutorialModel> tutorialModels) {
        final String csvHeader = "Topics, Subtopics, Links, Source\n";
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(csvHeader);
        for(TutorialModel tutorialModel: tutorialModels){
            csvContent.append(tutorialModel.getSubTopicsId().getTopicsID().getName()).append(", ")
                    .append(tutorialModel.getSubTopicsId().getName()).append(", ")
                    .append(tutorialModel.getLinks()).append(", ")
                    .append(tutorialModel.getSourceId().getName()).append("\n");
        }
        return csvContent.toString().getBytes();
    }
}
