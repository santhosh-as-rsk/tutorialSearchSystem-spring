package com.sequoia.tutorial.repository;

import java.util.List;
import java.util.Optional;

import com.sequoia.tutorial.models.SourceModel;
import com.sequoia.tutorial.models.SubTopicsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sequoia.tutorial.models.TutorialModel;
import org.springframework.data.jpa.repository.Query;

public interface TutorialRepository extends JpaRepository<TutorialModel, Long> {
    List<TutorialModel> findBySubTopicsId_TopicsId_NameAndSubTopicsId_NameAndActive(
            String topicsName,
            String subtopicsname,
            Boolean isActive,
            PageRequest page
    );
    List<TutorialModel> findBySubTopicsId_TopicsId_NameAndSubTopicsId_NameAndActive(
            String topicsName,
            String subtopicsname,
            Boolean isActive
    );

    @Query(value = "SELECT t FROM TutorialModel t JOIN t.sourceId s ORDER BY s.name",
            countQuery = "SELECT COUNT(t) FROM TutorialModel t")
    Page<TutorialModel> findAllWithPaginationAndSort(Pageable pageable);

    List<TutorialModel> findBySubTopicsId_NameInAndActiveIsTrue(
            List<String> subtopicsname
    );
    List<TutorialModel> findBySubTopicsId_NameInAndActiveIsTrue(
            List<String> subtopicsname,
            PageRequest page
    );

    TutorialModel findByLinksAndSubTopicsIdAndSourceId(String links, SubTopicsModel subTopicsModel, SourceModel sourceModel);

    default TutorialModel findOrCreateByLinksAndSubTopicsIdAndSourceId(String links, SubTopicsModel subTopicsModel, SourceModel sourceModel){
        return Optional.ofNullable(this.findByLinksAndSubTopicsIdAndSourceId(links,subTopicsModel,sourceModel))
                .orElseGet(()->{
                    TutorialModel tutorialModel = new TutorialModel();
                    tutorialModel.setLinks(links);
                    tutorialModel.setSubTopicsId(subTopicsModel);
                    tutorialModel.setSourceId(sourceModel);
                    tutorialModel.setActive(true);
                    return this.save(tutorialModel);
                });
    }
}


