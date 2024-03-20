package com.sequoia.tutorial.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sequoia.tutorial.models.ResponseData;
import jakarta.persistence.EntityNotFoundException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.data.domain.Sort;

import com.sequoia.tutorial.models.SourceModel;
import com.sequoia.tutorial.repository.SourceRepository;
import org.springframework.web.multipart.MultipartFile;

import static com.sequoia.tutorial.constants.constant.*;

@RestController
@CrossOrigin("*")
public class SourceController {
    Logger logger = LoggerFactory.getLogger(SourceController.class);
    @Autowired
    private SourceRepository sourceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * @return 
     */
    @GetMapping("/")
    public ResponseData viewSource(){
        ResponseData responseData = new ResponseData();
        Sort sort = Sort.by("name").ascending();
        List<SourceModel> sourceModel = sourceRepository.findAll(sort);
        System.out.println(sourceModel);
//        return sourceModel;
        logger.info("passed");
        responseData.setStatusCode(RESPONSE_CODE_SUCCESS);
        responseData.setMessage(RESPONSE_MESSAGE_SUCCESS);
        responseData.setOutputData(sourceModel);
        return responseData;
    }
    @PostMapping("/")
    public ResponseData updateSource(@RequestParam String source){
        System.out.println(source);
        logger.info(source);
        ResponseData responseData = new ResponseData();
        if (sourceRepository.findName(source)){
            responseData.setMessage(RESPONSE_MESSAGE_SUCCESS);
            responseData.setStatusCode(RESPONSE_CODE_SUCCESS);
            responseData.setOutputData("not saved");
            return responseData;
        }
        SourceModel sourceModel = new SourceModel();
        sourceModel.setName(source.toLowerCase());
        sourceModel.setActive(true);
        SourceModel savedSourceModel = sourceRepository.save(sourceModel);
        responseData.setMessage(RESPONSE_MESSAGE_SUCCESS);
        responseData.setStatusCode(RESPONSE_CODE_SUCCESS);
        responseData.setOutputData("saved successfully");
        return responseData;
    }
    @PostMapping("/trend-json")
    public ResponseEntity<byte[]> viewJson(@RequestParam MultipartFile file) {
        try {
            JSONObject jsonObject = new JSONObject();
            List<String> dates = new ArrayList<>();
            List<String> param = new ArrayList<>();
            InputStream string = file.getInputStream();

            JsonNode objectMapper1 = objectMapper.readTree((InputStream) string);
            JsonNode trendInfo = objectMapper1.get("trend_info");

            Iterator<String> iterator = trendInfo.fieldNames();
            iterator.forEachRemaining(e -> dates.add(e));

            for (String date : dates) {
                JsonNode dateKeys = trendInfo.get(date);
                Iterator<String> newIterator = dateKeys.fieldNames();
                newIterator.forEachRemaining(e -> {
                    if (!param.contains(e)) {
                        param.add(e);
                    }
                });
            }
            for (String par : param) {
                JSONObject jsonObject1 = new JSONObject();
                for (String date : dates) {
                    if (trendInfo.get(date).get(par) != null) jsonObject1.put(date, trendInfo.get(date).get(par));
                    else {
                        if (trendInfo.get(date).size() == 0) {
                            jsonObject1.put(date, new JSONObject());
                        }
                    }
                }
                jsonObject.put(par, jsonObject1);
            }
            byte[] bytes = jsonObject.toJSONString().getBytes();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentDispositionFormData("attachment", "trend_info.json");
            logger.info("file created successfully");
            return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/exceptions")
    public ResponseData exceptions(@RequestParam Integer number) throws JpaObjectRetrievalFailureException {
        try {
            switch (number) {
                case 0:
                    throw new RuntimeException("run time exception");
            }

            return new ResponseData(RESPONSE_CODE_SUCCESS,RESPONSE_MESSAGE_SUCCESS , "success");
        }catch (Exception e){
            return new ResponseData(RESPONSE_CODE_FAILURE, RESPONSE_MESSAGE_FAILURE, e.getMessage());
        }
    }
}

