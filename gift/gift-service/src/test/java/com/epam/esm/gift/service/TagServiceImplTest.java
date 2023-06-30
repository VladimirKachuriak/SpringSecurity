package com.epam.esm.gift.service;

import com.epam.esm.gift.model.Tag;
import com.epam.esm.gift.model.repo.TagDao;
import com.epam.esm.gift.service.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class TagServiceImplTest {
    @Mock
    private TagDao tagDao;

    @InjectMocks
    private TagServiceImpl tagServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void getAll() {
        List<Tag> tags = new ArrayList<>();
        Tag tag1 = new Tag();
        tag1.setId(1);
        tag1.setName("Tag1");
        Tag tag2 = new Tag();
        tag2.setId(1);
        tag2.setName("Tag1");
        tags.add(tag1);
        tags.add(tag2);

        when(tagDao.findAll()).thenReturn(tags);

        List<Tag> result = tagServiceImpl.getAll();

        assertEquals(tags.size(), result.size());
        assertEquals(tags.get(0), result.get(0));
        assertEquals(tags.get(1), result.get(1));

        verify(tagDao, times(1)).findAll();
        verifyNoMoreInteractions(tagDao);
    }

    @Test
    void getById() {
        Tag tag1 = new Tag();
        tag1.setId(3);
        tag1.setName("Tag1");
        when(tagDao.findById(3)).thenReturn(Optional.of(tag1));

        Tag result = tagServiceImpl.getById(3);

        assertEquals(tag1, result);

        verify(tagDao, times(1)).findById(3);
        verifyNoMoreInteractions(tagDao);
    }

    @Test
    void create() {
        Tag tag = new Tag();
        tag.setId(3);
        tag.setName("Tag3");

        when(tagDao.findByName(tag.getName())).thenReturn(null);
        when(tagDao.save(tag)).thenReturn(3);

        boolean result = tagServiceImpl.create(tag);

        assertTrue(result);

        verify(tagDao, times(1)).findByName(tag.getName());
        verify(tagDao, times(1)).save(tag);
        verifyNoMoreInteractions(tagDao);
    }

    @Test
    void update() {
        Tag tag = new Tag();
        tag.setId(3);
        tag.setName("Tag3");

        when(tagDao.findById(3)).thenReturn(Optional.of(tag));

        boolean result = tagServiceImpl.update(tag);

        assertTrue(result);

        verify(tagDao, times(1)).save(tag);
        verify(tagDao, times(1)).findById(3);
        verify(tagDao, times(1)).findByName("Tag3");
        verifyNoMoreInteractions(tagDao);
    }

    @Test
    void delete() {
        Tag tag = new Tag();
        tag.setId(3);
        tag.setName("Tag3");

        when(tagDao.findById(3)).thenReturn(Optional.of(tag));
        boolean result = tagServiceImpl.delete(3);

        assertTrue(result);
        verify(tagDao, times(1)).delete(3);
        verify(tagDao, times(1)).findById(3);
        verifyNoMoreInteractions(tagDao);
    }
}