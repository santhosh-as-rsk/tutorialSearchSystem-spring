package com.sequoia.tutorial.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sequoia.tutorial.models.SubTopicsModel;

public interface SubTopicsRepository extends JpaRepository<SubTopicsModel, Long> {
    List<SubTopicsModel> findByTopicsID_NameAndActive(String topicName, Boolean isActive,Sort sort);
}
