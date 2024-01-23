package com.sequoia.tutorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sequoia.tutorial.models.TopicsModel;

import java.util.Optional;

public interface TopicsRepository extends JpaRepository<TopicsModel,Long> {

    TopicsModel findByName(String topics);

    default TopicsModel findOrCreateByName(String topics){
        return Optional.ofNullable(this.findByName(topics))
                .orElseGet(()->{
                    TopicsModel topicsModel = new TopicsModel();
                    topicsModel.setName(topics);
                    topicsModel.setActive(true);
                    return this.save(topicsModel);
                });
    }
}
