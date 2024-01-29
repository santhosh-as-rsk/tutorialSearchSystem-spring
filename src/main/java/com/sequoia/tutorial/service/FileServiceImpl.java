package com.sequoia.tutorial.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sequoia.tutorial.models.*;
import com.sequoia.tutorial.repository.SourceRepository;
import com.sequoia.tutorial.repository.SubTopicsRepository;
import com.sequoia.tutorial.repository.TopicsRepository;
import com.sequoia.tutorial.repository.TutorialRepository;

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
    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
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
    @Transactional
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

            logger.info("DataSaved successfully");
            responseData.setStatusCode(RESPONSE_CODE_SUCCESS);
            responseData.setMessage(RESPONSE_MESSAGE_SUCCESS);
            responseData.setOutputData("Data Added Successfully");
        }catch (Exception e){
            logger.error(e.getMessage());
            responseData.setStatusCode(RESPONSE_CODE_FAILURE);
            responseData.setMessage(RESPONSE_MESSAGE_FAILURE);
            responseData.setOutputData(e.getMessage());
        }
        return responseData;
    }
}
