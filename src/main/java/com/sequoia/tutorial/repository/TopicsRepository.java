package com.sequoia.tutorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sequoia.tutorial.models.TopicsModel;

public interface TopicsRepository extends JpaRepository<TopicsModel,Long> {
}
