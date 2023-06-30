package com.epam.esm.gift.model.repo;

import com.epam.esm.gift.model.Certificate;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CertificateDao extends JpaRepository<Certificate, Integer> {
    @Query("SELECT c FROM Certificate c JOIN c.tags t WHERE t.name IN (:tags) GROUP BY c HAVING COUNT(DISTINCT t) = :count")
    List<Certificate> findByTags(List<String> tags, int count, PageRequest pageRequest);
}
