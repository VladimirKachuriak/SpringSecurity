package com.epam.esm.gift.service;

import com.epam.esm.gift.model.Tag;

import java.util.List;

/**
 * Service interface for managing Tag entities.
 */
public interface TagService extends Service<Tag> {
    /**
     * Retrieve a list of tags with pagination support.
     *
     * @param page The page number to retrieve.
     * @return A list of tags for the specified page.
     */
    public List<Tag> getAll(int page);
}
