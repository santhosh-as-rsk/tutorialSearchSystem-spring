package com.sequoia.tutorial.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sequoia.tutorial.models.TutorialModel;
import org.springframework.data.jpa.repository.Query;

public interface TutorialRepository extends JpaRepository<TutorialModel, Long> {
    List<TutorialModel> findBySubTopicsId_TopicsID_NameAndSubTopicsId_NameAndActive(
            String topicsName,
            String subtopicsname,
            Boolean isActive,
            PageRequest page
    );
    List<TutorialModel> findBySubTopicsId_TopicsID_NameAndSubTopicsId_NameAndActive(
            String topicsName,
            String subtopicsname,
            Boolean isActive
    );

    @Query(value = "SELECT t FROM TutorialModel t JOIN t.sourceId s ORDER BY s.name",
            countQuery = "SELECT COUNT(t) FROM TutorialModel t")
    Page<TutorialModel> findAllWithPaginationAndSort(Pageable pageable);
}

//    List<TutorialModel> findAll(PageRequest page);


