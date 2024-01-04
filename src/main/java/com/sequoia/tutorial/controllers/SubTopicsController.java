package com.sequoia.tutorial.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.sequoia.tutorial.models.SubTopicsModel;
import com.sequoia.tutorial.repository.SubTopicsRepository;

import java.util.List;

@RestController
@RequestMapping("api/")
@CrossOrigin("*")
public class SubTopicsController {
    private final SubTopicsRepository subTopicsRepository;

    @Autowired
    public SubTopicsController(SubTopicsRepository subTopicsRepository) {
        this.subTopicsRepository = subTopicsRepository;
    }

    @GetMapping("/fetchSubTopics")
    public List<SubTopicsModel> subTopicsTopic(@RequestParam String topicName){
        Sort sort = Sort.by("name").ascending();
        return subTopicsRepository.findByTopicsID_NameAndActive(topicName, true,sort);
    }
}
