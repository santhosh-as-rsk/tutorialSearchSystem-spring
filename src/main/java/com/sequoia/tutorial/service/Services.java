package com.sequoia.tutorial.service;


import com.sequoia.tutorial.models.ResponseData;

import java.util.List;

public interface Services {
    ResponseData fetchTopics();
    ResponseData fetchSubTopics(String topicName);

    ResponseData fetchMultipleSubTopics(List<String> topicNames);

    ResponseData fetchTutorialLinks(String topicName, String subTopicName, int page, int size);

    ResponseData fetchAllTutorialLink(int page, int size);

    ResponseData fetchTutorialLinkMultipleSubTopics(List<String> subTopicNames, int page, int size);
}
