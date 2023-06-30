package com.epam.esm.gift.service;

import com.epam.esm.gift.model.Certificate;
import com.epam.esm.gift.model.Purchase;
import com.epam.esm.gift.model.Role;
import com.epam.esm.gift.model.Tag;
import com.epam.esm.gift.model.User;
import com.epam.esm.gift.model.repo.CertificateDao;
import com.epam.esm.gift.model.repo.UserDao;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserDao userDao;
    private final CertificateDao certificateDao;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserDao userDao, CertificateDao certificateDao, JwtService jwtService, @Lazy AuthenticationManager authenticationManager) {
        this.userDao = userDao;
        this.certificateDao = certificateDao;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Tag getMostUsedTag(int userId) {
        return userDao.findMostUsedTagByUser(userId);
    }

    @Override
    public List<User> getAll() {
        return userDao.findAll();
    }

    @Override
    public User getById(int id) {
        return userDao.findById(id).orElse(null);
    }

    @Override
    public boolean create(User user) {
        user.setRole(Set.of(Role.USER));
        userDao.save(user);
        return true;
    }

    @Override
    public boolean update(User user) {
        User userDb = userDao.findById(user.getId()).orElse(null);
        if (userDb == null) return false;
        user.setAccount(user.getAccount());
        userDao.save(userDb);
        return true;
    }

    @Override
    public boolean delete(int id) {
        User userDb = userDao.findById(id).orElse(null);
        if (userDb == null) return false;
        userDao.delete(userDb);
        return true;
    }

    @Transactional
    public boolean makePurchase(int userId, int certificateId) {
        User user = userDao.findById(userId).orElse(null);
        Certificate certificate = certificateDao.findById(certificateId).orElse(null);
        if (user == null || certificate == null) return false;
        if (user.getAccount() < certificate.getPrice()) return false;
        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setCertificate(certificate);
        purchase.setPrice(certificate.getPrice());
        purchase.setPurchaseDate(LocalDateTime.now());
        user.setAccount(user.getAccount() - certificate.getPrice());
        user.getPurchases().add(purchase);
        userDao.save(user);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.findByName(username);
    }
}
