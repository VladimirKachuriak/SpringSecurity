package com.epam.esm.gift.service;

import com.epam.esm.gift.model.Tag;
import com.epam.esm.gift.model.User;

/**
 * Service interface for managing users.
 * Extends the generic Service interface.
 */
public interface UserService extends Service<User> {
    /**
     * Retrieves the most used tag by the user with the given ID.
     *
     * @param userId The ID of the user.
     * @return The most used tag by the user.
     */
    Tag getMostUsedTag(int userId);

    /**
     * Makes a purchase for the user with the specified user ID
     * to acquire the certificate with the given certificate ID.
     *
     * @param userId        The ID of the user making the purchase.
     * @param certificateId The ID of the certificate being purchased.
     * @return true if the purchase is successful, false otherwise.
     */
    boolean makePurchase(int userId, int certificateId);
}
