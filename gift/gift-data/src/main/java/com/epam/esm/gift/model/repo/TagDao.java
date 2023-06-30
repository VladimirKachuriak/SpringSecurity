package com.epam.esm.gift.model.repo;

import com.epam.esm.gift.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagDao extends JpaRepository<Tag, Integer> {
    Tag findByName(String name);
    Page<Tag> findAll(Pageable pageable);
}
