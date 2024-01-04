package com.sequoia.tutorial.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import com.sequoia.tutorial.models.TopicsModel;
import com.sequoia.tutorial.repository.TopicsRepository;


@RestController
@RequestMapping("api/")
@CrossOrigin("*")
public class TopicsController {
    @Autowired
    private TopicsRepository topicsRepository;

    @GetMapping("/fetchTopics")
    public List<TopicsModel> topicsModelList(){;
        Sort sort = Sort.by("name").ascending();
        List<TopicsModel> topicsModel = topicsRepository.findAll(sort);
        return topicsModel;
    }

}
