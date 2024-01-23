package com.sequoia.tutorial.repository;

import com.sequoia.tutorial.models.SourceModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SourceRepository extends JpaRepository<SourceModel, Long> {
    SourceModel findByName(String name);

    default SourceModel findOrCreateSource(String source) {
        return Optional.ofNullable(this.findByName(source))
                .orElseGet(() -> {
                    SourceModel sourceModel = new SourceModel();
                    sourceModel.setName(source);
                    sourceModel.setActive(true);
                    return this.save(sourceModel);
                });
    }
}
