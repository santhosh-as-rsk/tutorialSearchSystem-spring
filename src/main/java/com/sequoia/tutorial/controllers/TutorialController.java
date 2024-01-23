package com.sequoia.tutorial.controllers;

import java.io.IOException;
import java.util.List;

import com.sequoia.tutorial.models.ResponseData;
import com.sequoia.tutorial.models.TutorialModel;
import com.sequoia.tutorial.service.FileService;
import com.sequoia.tutorial.service.Services;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestDataValueProcessor;


@RestController
@RequestMapping("api/")
@CrossOrigin("*")
public class TutorialController {
    @Autowired
    Services services;
    @Autowired
    FileService excelService;

    /**
     * @param topicName
     * @param subTopicName
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/fetchTutorialLinks")
    public ResponseData tutorialLinks(
            @RequestParam String topicName,
            @RequestParam String subTopicName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return services.fetchTutorialLinks(topicName, subTopicName, page, size);
    }

    /**
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/fetchAllTutorialLink")
    public ResponseData getAllWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return services.fetchAllTutorialLink(page,size);
    }

    /**
     * @param subTopicNames
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/fetchTutorialLinkMultipleSubTopics")
    public @ResponseBody ResponseData getTutorialLinksMultipleSubTopics(
            @RequestParam List<String> subTopicNames,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return services.fetchTutorialLinkMultipleSubTopics(subTopicNames,page,size);
    }

    @GetMapping("/fetchAll")
    public void gettutorial(HttpServletResponse res) throws IOException {
            excelService.getExcelSheet( res);
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportTutorialsToExcel() throws IOException {
        byte[] excelBytes = excelService.getAllTutorialsAsExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "tutorials.xlsx");

        return new ResponseEntity<>(excelBytes, headers, org.springframework.http.HttpStatus.OK);
    }
    @PostMapping("fetchExcelFile")
    public ResponseEntity<byte[]> exportasExcelFile(
            @RequestBody List<TutorialModel> data
            ) {
        byte[] excelBytes = excelService.getTutorialsAsExcel(data);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "tutorials.xlsx");
        return new ResponseEntity<>(excelBytes, headers, org.springframework.http.HttpStatus.OK);
    }

    @PostMapping("fetchCsvFile")
    public ResponseEntity<byte[]> exportasCsvFile(
            @RequestBody List<TutorialModel> data
    ){
        byte[] csvBytes = excelService.getTutorialsAsCsv(data);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "tuorials.csv");
        return  new ResponseEntity<>(csvBytes, headers, org.springframework.http.HttpStatus.OK);
    }

    @PostMapping(value="/importExcelFile",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData importExcelFile(@RequestParam MultipartFile file){
        System.out.println(file.getOriginalFilename());
        return new ResponseData();
    }
    @PostMapping("/upload")
    public ResponseData handleFileUpload(@RequestParam("file") MultipartFile file) {
        return excelService.importExcelFile(file);
    }
}
