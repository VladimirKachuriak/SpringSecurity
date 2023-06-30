package com.epam.esm.gift.service;

import com.epam.esm.gift.model.Certificate;
import com.epam.esm.gift.model.Tag;
import com.epam.esm.gift.model.User;
import com.epam.esm.gift.model.repo.CertificateDao;
import com.epam.esm.gift.model.repo.UserDao;
import com.epam.esm.gift.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    @Mock
    private CertificateDao certificateDao;
    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void getMostUsedTag() {
        Tag tag = new Tag();
        tag.setId(1);
        tag.setName("Metro");
        when(userDao.findMostUsedTagByUser(1)).thenReturn(tag);

        Tag tagRes = userServiceImpl.getMostUsedTag(1);
        assertEquals("Tag{id=1, name='Metro'}", tagRes.toString());

        verify(userDao,times(1)).findMostUsedTagByUser(1);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void getById() {
        User user = new User();
        user.setId(1);
        user.setName("user1");
        user.setAccount(2000);
        when(userDao.findById(1)).thenReturn(Optional.of(user));
        assertEquals("user1", userServiceImpl.getById(1).getName());

        verify(userDao,times(1)).findById(1);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void makePurchase() {
        User user = new User();
        user.setId(1);
        user.setName("user1");
        user.setAccount(2000);
        user.setPurchases(new ArrayList<>());
        when(certificateDao.findById(1)).thenReturn(Optional.of(new Certificate()));
        when(userDao.findById(1)).thenReturn(Optional.of(user));

        Boolean result = userServiceImpl.makePurchase(1 ,1);
        assertEquals(true, result);

        verify(userDao,times(1)).findById(1);
        verify(certificateDao,times(1)).findById(1);
        verify(userDao,times(1)).save(any(User.class));
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void getAll() {
        User user1 = new User();
        user1.setId(1);
        user1.setName("user1");
        User user2 = new User();
        user2.setId(2);
        user2.setName("user2");
        when(userDao.findAll()).thenReturn(List.of(user1,user2));
        List<User> users= userServiceImpl.getAll();
        assertEquals(2, users.size());
        verify(userDao,times(1)).findAll();
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void create() {
        User user = new User();
        user.setName("user1");
        user.setAccount(2000);
        user.setPurchases(new ArrayList<>());
        Boolean result = userServiceImpl.create(user);
        assertEquals(true, result);
        verify(userDao,times(1)).save(any(User.class));
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void update() {
        User user = new User();
        user.setId(1);
        user.setName("user1");
        user.setAccount(2000);
        user.setPurchases(new ArrayList<>());
        when(userDao.findById(1)).thenReturn(Optional.of(user));
        Boolean result = userServiceImpl.update(user);
        assertEquals(true, result);
        verify(userDao,times(1)).save(any(User.class));
        verify(userDao,times(1)).findById(1);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void delete() {
        User user = new User();
        user.setId(1);
        user.setName("user1");
        user.setAccount(2000);
        user.setPurchases(new ArrayList<>());
        when(userDao.findById(1)).thenReturn(Optional.of(user));
        Boolean result = userServiceImpl.delete(1);
        assertEquals(true, result);
        verify(userDao,times(1)).delete(1);
        verify(userDao,times(1)).findById(1);
        verifyNoMoreInteractions(userDao);
    }
}