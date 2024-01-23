package com.sequoia.tutorial.service;

import com.sequoia.tutorial.models.*;
import com.sequoia.tutorial.repository.SourceRepository;
import com.sequoia.tutorial.repository.SubTopicsRepository;
import com.sequoia.tutorial.repository.TopicsRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import com.sequoia.tutorial.repository.TutorialRepository;
import org.springframework.web.multipart.MultipartFile;

import static com.sequoia.tutorial.constants.constant.*;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    TutorialRepository tutorialRepository;

    @Autowired
    TopicsRepository topicsRepository;

    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    SubTopicsRepository subTopicsRepository;

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
                row.createCell(1).setCellValue(tutorialModel.getSubTopicsId().getTopicsId().getName());
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
                row.createCell(0).setCellValue(tutorial.getSubTopicsId().getTopicsId().getName());
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
                row.createCell(0).setCellValue(tutorial.getSubTopicsId().getTopicsId().getName());
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
            csvContent.append(tutorialModel.getSubTopicsId().getTopicsId().getName()).append(", ")
                    .append(tutorialModel.getSubTopicsId().getName()).append(", ")
                    .append(tutorialModel.getLinks()).append(", ")
                    .append(tutorialModel.getSourceId().getName()).append("\n");
        }
        return csvContent.toString().getBytes();
    }

    @Override
    public ResponseData importExcelFile(MultipartFile file){
        List<String> headerValues = new ArrayList<>();
        ResponseData responseData = new ResponseData();
        try{
            String newValue = "";
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            Iterator<Row> rowIterator = sheet.rowIterator();

            if (rowIterator.hasNext()) {
                Row headerRow = rowIterator.next();

                Iterator<Cell> headerCellIterator = headerRow.cellIterator();
                while (headerCellIterator.hasNext()) {
                    Cell headerCell = headerCellIterator.next();
                    String headerCellValue = dataFormatter.formatCellValue(headerCell);
                    headerValues.add(headerCellValue);
                }
                List<String> expectedHeader = Arrays.asList("Topics", "SubTopics", "Links", "Source");
                if (!headerValues.equals(expectedHeader)) {
                    throw new RuntimeException("Invalid header. Expected: " + expectedHeader + ", Actual: " + headerValues);
                }
            }

            while (rowIterator.hasNext()){
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                String topics = dataFormatter.formatCellValue(cellIterator.next());
                String subTopics = dataFormatter.formatCellValue(cellIterator.next());
                String links = dataFormatter.formatCellValue(cellIterator.next());
                String source = dataFormatter.formatCellValue((cellIterator.next()));
                SourceModel sourceModel = sourceRepository.findOrCreateSource(source.toLowerCase());
                TopicsModel topicsModel = topicsRepository.findOrCreateByName(topics.toLowerCase());
                SubTopicsModel subTopicsModel = subTopicsRepository.findOrCreateByNameAndTopicsId(subTopics.toLowerCase(),topicsModel);
                TutorialModel tutorialModel = tutorialRepository.findOrCreateByLinksAndSubTopicsIdAndSourceId(links.toLowerCase(),subTopicsModel, sourceModel);
            }
            System.out.println(sheet.getSheetName());
            responseData.setStatusCode(RESPONSE_CODE_SUCCESS);
            responseData.setMessage(RESPONSE_MESSAGE_SUCCESS);
            responseData.setOutputData("Data Added Successfully");
        }catch (Exception e){
            responseData.setStatusCode(RESPONSE_CODE_FAILURE);
            responseData.setMessage(RESPONSE_MESSAGE_FAILURE);
        }
        return responseData;
    }
}
