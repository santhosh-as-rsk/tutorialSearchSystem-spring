package com.sequoia.tutorial.service;

import java.util.HashMap;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sequoia.tutorial.models.ResponseData;
import com.sequoia.tutorial.models.SubTopicsModel;
import com.sequoia.tutorial.models.TopicsModel;
import com.sequoia.tutorial.models.TutorialModel;
import com.sequoia.tutorial.repository.SubTopicsRepository;
import com.sequoia.tutorial.repository.TopicsRepository;
import com.sequoia.tutorial.repository.TutorialRepository;

import static com.sequoia.tutorial.constants.constant.*;

@Service
public class ServicesImpl implements Services {
    Logger logger = LoggerFactory.getLogger(ServicesImpl.class);
    @Autowired
    TopicsRepository topicsRepository;
    @Autowired
    SubTopicsRepository subTopicsRepository;

    @Autowired
    TutorialRepository tutorialRepository;

    /**
     * @return
     */
    @Override
    public ResponseData fetchTopics() {
        ResponseData responseData = new ResponseData();
        try{
            Sort sort = Sort.by("name").ascending();
            List<TopicsModel> topicsModel = topicsRepository.findAll(sort);
            logger.info("Topics data fetched successfully");
            responseData.setStatusCode(RESPONSE_CODE_SUCCESS);
            responseData.setMessage(RESPONSE_MESSAGE_SUCCESS);
            responseData.setOutputData(topicsModel);
        }catch (Exception e){
            logger.error(e.getMessage());
            responseData.setStatusCode(RESPONSE_CODE_FAILURE);
            responseData.setMessage(RESPONSE_MESSAGE_FAILURE);
        }
        return responseData;
    }

    /**
     * @param topicName
     * @return
     */
    @Override
    public ResponseData fetchSubTopics(String topicName){
        ResponseData responseData = new ResponseData();
        try{
            Sort sort = Sort.by("name").ascending();
            List<SubTopicsModel> subTopicsModelList = subTopicsRepository.findByTopicsId_NameAndActive(topicName, true,sort);
            if (subTopicsModelList.size() == 0){ throw  new EntityNotFoundException("Entity not found for given topic name -> "+ topicName);}
            responseData.setStatusCode(RESPONSE_CODE_SUCCESS);
            responseData.setMessage(RESPONSE_MESSAGE_SUCCESS);
            responseData.setOutputData(subTopicsModelList);
            logger.info("Sub Topics data fetched successfully");
        }catch (Exception e){
            logger.error(e.getMessage());
            responseData.setStatusCode(RESPONSE_CODE_FAILURE);
            responseData.setMessage(RESPONSE_MESSAGE_FAILURE);
            responseData.setOutputData(e.getMessage());
        }
        return responseData;
    }

    /**
     * @param topicNames
     * @return
     */
    @Override
    public ResponseData fetchMultipleSubTopics(List<String> topicNames){
        ResponseData responseData = new ResponseData();
        try{
            Sort sort = Sort.by("name").ascending();
            List<SubTopicsModel> subTopicsModelList = subTopicsRepository.findByTopicsId_NameInAndActiveIsTrue(topicNames,sort);
            responseData.setStatusCode(RESPONSE_CODE_SUCCESS);
            responseData.setMessage(RESPONSE_MESSAGE_SUCCESS);
            responseData.setOutputData(subTopicsModelList);
            logger.info("subtopics fetched successfully");
        }catch (Exception e){
            logger.error(e.getMessage());
            responseData.setStatusCode(RESPONSE_CODE_FAILURE);
            responseData.setMessage(RESPONSE_MESSAGE_FAILURE);

        }
        return responseData;
    }

    /**
     * @param topicName
     * @param subTopicName
     * @param page
     * @param size
     * @return
     */
    @Override
    public ResponseData fetchTutorialLinks(String topicName, String subTopicName, int page, int size){
        ResponseData responseData = new ResponseData();
        try {
            PageRequest pages = PageRequest.of(page, size);
            List<TutorialModel> paginatedTutorialModels = tutorialRepository.findBySubTopicsId_TopicsId_NameAndSubTopicsId_NameAndActive(
                    topicName,
                    subTopicName,
                    true,
                    pages
            );
            Long totalCount = Long.valueOf(tutorialRepository.findBySubTopicsId_TopicsId_NameAndSubTopicsId_NameAndActive(
                    topicName,
                    subTopicName,
                    true
            ).size());
            int totalPage = (int) Math.ceil((double) totalCount / size);
            HashMap<String, Object> outputData = new HashMap<>();
            outputData.put("toltalCount", totalCount);
            outputData.put("totalPage", totalPage);
            outputData.put("currentPage", page);
            outputData.put("tutorialLink", paginatedTutorialModels);
            responseData.setStatusCode(RESPONSE_CODE_SUCCESS);
            responseData.setMessage(RESPONSE_MESSAGE_SUCCESS);
            responseData.setOutputData(outputData);
            logger.info("tutorial links fetched successfully");
        }catch (Exception e){
            logger.error(e.getMessage());
            responseData.setStatusCode(RESPONSE_CODE_FAILURE);
            responseData.setMessage(RESPONSE_MESSAGE_FAILURE);
        }
        return responseData;
    }

    /**
     * @param page
     * @param size
     * @return
     */
    @Override
    public ResponseData fetchAllTutorialLink(int page, int size){
        ResponseData responseData = new ResponseData();
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<TutorialModel> pageResult = tutorialRepository.findAllWithPaginationAndSort(pageable);
            Long totoalCount = Long.valueOf(tutorialRepository.findAll().size());
            int totalPage = (int) Math.ceil((double) totoalCount / size);
            HashMap<String, Object> outputData = new HashMap<>();
            outputData.put("totalCount", totoalCount);
            outputData.put("totalPage", totalPage);
            outputData.put("currentPage", page);
            outputData.put("tutorialLink", pageResult.getContent());
            responseData.setStatusCode(RESPONSE_CODE_SUCCESS);
            responseData.setMessage(RESPONSE_MESSAGE_SUCCESS);
            responseData.setOutputData(outputData);
            logger.info("tutorial links fetched successfully");
        }catch (Exception e){
            logger.error(e.getMessage());
            responseData.setStatusCode(RESPONSE_CODE_FAILURE);
            responseData.setMessage(RESPONSE_MESSAGE_FAILURE);
        }
        return responseData;
    }

    /**
     * @param subTopicNames 
     * @param page
     * @param size
     * @return
     */
    @Override
    public ResponseData fetchTutorialLinkMultipleSubTopics(List<String> subTopicNames, int page, int size){
        ResponseData responseData = new ResponseData();
        try{
            PageRequest pages = PageRequest.of(page,size);
            List<TutorialModel> paginatedTutorialModels = tutorialRepository.findBySubTopicsId_NameInAndActiveIsTrue(
                    subTopicNames,
                    pages
            );

            Long totalCount = Long.valueOf(tutorialRepository.findBySubTopicsId_NameInAndActiveIsTrue(subTopicNames).size());
            int totalPage = (int) Math.ceil((double) totalCount / size);
            HashMap<String, Object> outputData = new HashMap<>();
            outputData.put("toltalCount", totalCount);
            outputData.put("totalPage", totalPage);
            outputData.put("currentPage", page);
            outputData.put("tutorialLink", paginatedTutorialModels);

            responseData.setStatusCode(RESPONSE_CODE_SUCCESS);
            responseData.setMessage(RESPONSE_MESSAGE_SUCCESS);
            responseData.setOutputData(outputData);
            logger.info("tutorial links fetched successfully");
        }catch (Exception e){
            logger.error(e.getMessage());
            responseData.setStatusCode(RESPONSE_CODE_FAILURE);
            responseData.setMessage(RESPONSE_MESSAGE_FAILURE);
        }
        return responseData;
    }
}
