package com.epam.esm.gift.model.repo;

import com.epam.esm.gift.model.Tag;
import com.epam.esm.gift.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository<User, Integer> {
    @Query("SELECT t FROM User u JOIN u.purchases p JOIN p.certificate c JOIN c.tags t " +
            "WHERE u.id = :userId GROUP BY t ORDER BY COUNT(t) DESC, SUM(p.price) DESC LIMIT 1")
    Tag findMostUsedTagByUser(int userId);
    User findByName(String name);
}
