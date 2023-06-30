package com.epam.esm.gift.service;

import com.epam.esm.gift.model.Certificate;
import com.epam.esm.gift.model.Purchase;
import com.epam.esm.gift.model.User;
import com.epam.esm.gift.model.repo.PurchaseDao;
import com.epam.esm.gift.service.PurchaseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class PurchaseServiceImplTest {
    @Mock
    private PurchaseDao purchaseDao;
    @InjectMocks
    private PurchaseServiceImpl purchaseServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll() {
        List<Purchase> purchases = new ArrayList<>();
        Purchase purchase1 = new Purchase();
        purchase1.setUser(new User());
        purchase1.setCertificate(new Certificate());
        Purchase purchase2 = new Purchase();
        purchase2.setUser(new User());
        purchase2.setCertificate(new Certificate());
        purchases.add(purchase1);
        purchases.add(purchase2);
        when(purchaseDao.findAll()).thenReturn(purchases);

        List<Purchase> result = purchaseServiceImpl.getAll();

        assertEquals(purchases.size(), result.size());

        verify(purchaseDao, times(1)).findAll();
        verifyNoMoreInteractions(purchaseDao);
    }

    @Test
    void testGetAll() {
        List<Purchase> purchases = new ArrayList<>();
        Purchase purchase1 = new Purchase();
        purchase1.setUser(new User());
        purchase1.setCertificate(new Certificate());
        Purchase purchase2 = new Purchase();
        purchase2.setUser(new User());
        purchase2.setCertificate(new Certificate());
        purchases.add(purchase1);
        purchases.add(purchase2);

        when(purchaseDao.findAll(PageRequest.of(2,5))).thenReturn(purchases);

        List<Purchase> result = purchaseServiceImpl.getAll(3);

        assertEquals(purchases.size(), result.size());

        verify(purchaseDao, times(1)).findAll(PageRequest.of(2,5));
        verifyNoMoreInteractions(purchaseDao);
    }

    @Test
    void create() {
        Purchase purchase = new Purchase();


        when(purchaseDao.save(purchase)).thenReturn(3);

        boolean result = purchaseServiceImpl.create(purchase);

        assertTrue(result);

        verify(purchaseDao, times(1)).save(purchase);
        verifyNoMoreInteractions(purchaseDao);
    }

    @Test
    void update() {
        Purchase purchase = new Purchase();
        purchase.setId(3);
        purchase.setUser(new User());
        purchase.setCertificate(new Certificate());
        when(purchaseDao.findById(3)).thenReturn(Optional.of(purchase));
        boolean result = purchaseServiceImpl.update(purchase);

        assertTrue(result);

        verify(purchaseDao, times(1)).findById(3);
        verify(purchaseDao, times(1)).save(any(Purchase.class));
        verifyNoMoreInteractions(purchaseDao);
    }

    @Test
    void delete() {
        when(purchaseDao.findById(3)).thenReturn(Optional.of(new Purchase()));
        boolean result = purchaseServiceImpl.delete(3);

        assertTrue(result);

        verify(purchaseDao, times(1)).findById(3);
        verify(purchaseDao, times(1)).delete(3);
        verifyNoMoreInteractions(purchaseDao);
    }
}