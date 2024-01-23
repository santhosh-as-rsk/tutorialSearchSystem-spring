package com.sequoia.tutorial.repository;

import java.util.List;
import java.util.Optional;

import com.sequoia.tutorial.models.TopicsModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sequoia.tutorial.models.SubTopicsModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubTopicsRepository extends JpaRepository<SubTopicsModel, Long> {
    List<SubTopicsModel> findByTopicsId_NameAndActive(String topicName, Boolean isActive,Sort sort);

    List<SubTopicsModel> findByTopicsId_NameInAndActiveIsTrue(List<String> topicNames, Sort sort);

    SubTopicsModel findByNameAndTopicsId(String subTopic, TopicsModel topic);

    default SubTopicsModel findOrCreateByNameAndTopicsId(String subTopic, TopicsModel topic){
        return Optional.ofNullable(this.findByNameAndTopicsId(subTopic, topic))
                .orElseGet(()->{
                    SubTopicsModel subTopicsModel = new SubTopicsModel();
                    subTopicsModel.setTopicsId(topic);
                    subTopicsModel.setName(subTopic);
                    subTopicsModel.setActive(true);
                    return this.save(subTopicsModel);
                });
    }
}




