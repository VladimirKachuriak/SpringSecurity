package com.epam.esm.gift.service;

import com.epam.esm.gift.model.Certificate;
import com.epam.esm.gift.model.Purchase;
import com.epam.esm.gift.model.repo.PurchaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseDao purchaseDao;

    @Autowired
    public PurchaseServiceImpl(PurchaseDao purchaseDao) {
        this.purchaseDao = purchaseDao;
    }

    @Override
    public List<Purchase> getAll() {
        return purchaseDao.findAll();
    }

    @Override
    public List<Purchase> getAll(int page) {
        List<Certificate> certificates;
        if (page < 1) {
            page = 1;
        }
        page--;
        return purchaseDao.findAll(PageRequest.of(page, 5)).stream().toList();
    }

    @Override
    public Purchase getById(int id) {
        return purchaseDao.findById(id).orElse(null);
    }

    @Override
    public boolean create(Purchase purchase) {
        purchaseDao.save(purchase);
        return true;
    }

    @Override
    public boolean update(Purchase purchase) {
        Purchase purchaseDb = purchaseDao.findById(purchase.getId()).orElse(null);
        if (purchaseDb == null) return false;
        purchaseDao.save(purchase);
        return true;
    }

    @Override
    public boolean delete(int id) {
        Purchase purchase = purchaseDao.findById(id).orElse(null);
        if (purchase == null) return false;
        purchaseDao.delete(purchase);
        return true;
    }
}
