package com.epam.esm.gift.service;

import com.epam.esm.gift.model.Certificate;

import java.util.List;
/**
 * Service interface for managing Certificate entities.
 */
public interface CertificateService extends Service<Certificate>{
    /**
     * Retrieve a list of certificates with pagination support.
     *
     * @param page The page number to retrieve.
     * @return A list of certificates for the specified page.
     */
    public List<Certificate> getAll(int page, List<String> tags);
}
