package com.sequoia.tutorial.controllers;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.sequoia.tutorial.models.TutorialModel;
import com.sequoia.tutorial.repository.TutorialRepository;

@RestController
@RequestMapping("api/")
@CrossOrigin("*")
public class TutorialController {
    @Autowired
    private TutorialRepository tutorialRepository;

//    @GetMapping("/")
//    public List<TutorialModel> tutorialModellist(){
//
//        List<TutorialModel> tutorialModels = tutorialRepository.findAll();
//        System.out.println(tutorialModels);
//        return tutorialModels;
//    }

    @GetMapping("/fetchTutorialLinks")
    public HashMap<String, Object> tutorialLinksByModel(
            @RequestParam String topicName,
            @RequestParam String subTopicName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        PageRequest pages = PageRequest.of(page,size);
        List<TutorialModel> paginatedTutorialModels = tutorialRepository.findBySubTopicsId_TopicsID_NameAndSubTopicsId_NameAndActive(
                topicName,
                subTopicName,
                true,
                pages
        );
        Long totalCount = Long.valueOf(tutorialRepository.findBySubTopicsId_TopicsID_NameAndSubTopicsId_NameAndActive(
                topicName,
                subTopicName,
                true
        ).size());
        int totalPage = (int) Math.ceil((double) totalCount / size);
        HashMap<String, Object> response = new HashMap<>();
        response.put("toltalCount", totalCount);
        response.put("totalPage", totalPage);
        response.put("currentPage", page);
        response.put("tutorialLink", paginatedTutorialModels);
        return response;
    }

//    @GetMapping("/fetchAllTutorialLink")
//    public HashMap<String, Object> allTutorialLink(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ){
//        System.out.println(page);
//        Pageable pages = PageRequest.of(page,size, Sort.by("sourceId.name"));
//        Page paginatedtutorialModel = (Page) tutorialRepository.findAll(pages);
//        Long totoalCount = Long.valueOf(tutorialRepository.findAll().size());
//        int totalPage = (int) Math.ceil((double) totoalCount/size);
//        HashMap<String, Object> response = new HashMap<>();
//        response.put("totalCount", totoalCount);
//        response.put("totalPage", totalPage);
//        response.put("tutorialLink", paginatedtutorialModel);
//        return response;
//    }

    @GetMapping("/fetchAllTutorialLink")
    public HashMap<String, Object> getAllWithPaginationAndSort(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TutorialModel> pageResult = tutorialRepository.findAllWithPaginationAndSort(pageable);
        Long totoalCount = Long.valueOf(tutorialRepository.findAll().size());
        int totalPage = (int) Math.ceil((double) totoalCount/size);
        HashMap<String, Object> response = new HashMap<>();
        response.put("totalCount", totoalCount);
        response.put("totalPage", totalPage);
        response.put("currentPage", page);
        response.put("tutorialLink", pageResult.getContent());
        return response;
    }
}
