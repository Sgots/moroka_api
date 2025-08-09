package com.springboot.backend.repository;

import com.springboot.backend.model.Topics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topics, Long> {
    // Add custom query methods if needed

}
