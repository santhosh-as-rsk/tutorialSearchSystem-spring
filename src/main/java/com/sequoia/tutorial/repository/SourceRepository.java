package com.sequoia.tutorial.repository;

import com.sequoia.tutorial.models.SourceModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SourceRepository extends JpaRepository<SourceModel, Long> {
}
