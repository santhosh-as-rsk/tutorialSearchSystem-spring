package com.sequoia.tutorial.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Sort;

import com.sequoia.tutorial.models.SourceModel;
import com.sequoia.tutorial.repository.SourceRepository;

@RestController
@CrossOrigin("*")
public class SourceController {
    @Autowired
    private SourceRepository sourceRepository;
    @GetMapping("/")
    public List<SourceModel> viewSource(){
        Sort sort = Sort.by("name").ascending();
        List<SourceModel> sourceModel = sourceRepository.findAll(sort);
        System.out.println(sourceModel);
        return sourceModel;
    }


}
