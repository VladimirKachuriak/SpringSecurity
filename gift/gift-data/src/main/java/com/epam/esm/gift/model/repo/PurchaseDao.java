package com.epam.esm.gift.model.repo;

import com.epam.esm.gift.model.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PurchaseDao extends JpaRepository<Purchase, Integer> {
    Page<Purchase> findAll(Pageable pageable);
}
