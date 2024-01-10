package com.sequoia.tutorial.controllers;

import java.util.HashMap;
import java.util.List;

import com.sequoia.tutorial.models.ResponseData;
import com.sequoia.tutorial.service.Services;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import com.sequoia.tutorial.models.TutorialModel;
import com.sequoia.tutorial.repository.TutorialRepository;

@RestController
@RequestMapping("api/")
@CrossOrigin("*")
public class TutorialController {
    @Autowired
    Services services;

    @GetMapping("/fetchTutorialLinks")
    public ResponseData tutorialLinks(
            @RequestParam String topicName,
            @RequestParam String subTopicName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return services.fetchTutorialLinks(topicName, subTopicName, page, size);
    }

    @GetMapping("/fetchAllTutorialLink")
    public ResponseData getAllWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return services.fetchAllTutorialLink(page,size);
    }

    @GetMapping("/fetchTutorialLinkMultipleSubTopics")
    public @ResponseBody ResponseData getTutorialLinksMultipleSubTopics(
            @RequestParam List<String> subTopicNames,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return services.fetchTutorialLinkMultipleSubTopics(subTopicNames,page,size);
    }

}
