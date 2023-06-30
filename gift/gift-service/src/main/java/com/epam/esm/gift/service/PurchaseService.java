package com.epam.esm.gift.service;

import com.epam.esm.gift.model.Purchase;

import java.util.List;
/**
 * Service interface for managing Purchase entities.
 */
public interface PurchaseService extends Service<Purchase>{
    /**
     * Retrieve a list of purchases with pagination support.
     *
     * @param page The page number to retrieve.
     * @return A list of purchases for the specified page.
     */
    List<Purchase> getAll(int page);
}
