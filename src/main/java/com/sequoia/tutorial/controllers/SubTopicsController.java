package com.sequoia.tutorial.controllers;

import com.sequoia.tutorial.models.ResponseData;
import com.sequoia.tutorial.service.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/")
@CrossOrigin("*")
public class SubTopicsController {
    @Autowired
    Services services;

    /**
     * @param topicName
     * @return
     */
    @GetMapping("/fetchSubTopics")
    public ResponseData subTopicsTopic(@RequestParam String topicName){
        return services.fetchSubTopics(topicName);
    }

    /**
     * @param topicNames
     * @return
     */
    @GetMapping("/fetchMultipleSubTopics")
    public  ResponseData multipleSubtopics(@RequestParam List<String> topicNames){
        return services.fetchMultipleSubTopics(topicNames);
    }
}
