package com.sequoia.tutorial.controllers;

import com.sequoia.tutorial.models.ResponseData;
import com.sequoia.tutorial.service.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/")
@CrossOrigin("*")
public class TopicsController {
    @Autowired
    Services services;

    @GetMapping("/fetchTopics")
    public ResponseData topicsModelList(){
        return services.fetchTopics();
    }

}
