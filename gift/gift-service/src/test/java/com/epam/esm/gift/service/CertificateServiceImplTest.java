package com.epam.esm.gift.service;

import com.epam.esm.gift.model.Certificate;
import com.epam.esm.gift.model.Tag;
import com.epam.esm.gift.model.repo.CertificateDao;
import com.epam.esm.gift.model.repo.TagDao;
import com.epam.esm.gift.service.CertificateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class CertificateServiceImplTest {
    @Mock
    private CertificateDao certificateDao;
    @Mock
    private TagDao tagDao;

    @InjectMocks
    private CertificateServiceImpl certificateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll() {
        when(certificateDao.findAll()).thenReturn(List.of(new Certificate(), new Certificate()));

        assertEquals(2, certificateService.getAll().size());

        verify(certificateDao, times(1)).findAll();
        verifyNoMoreInteractions(certificateDao);
    }

    @Test
    void testGetAll() {
        List<String> tags = new ArrayList<>(List.of("Tag1", "Tag2"));
        when(certificateDao.findByTags(tags, tags.size(), PageRequest.of(0, 5))).thenReturn(List.of(new Certificate(), new Certificate()));

        assertEquals(2, certificateService.getAll(1, tags).size());

        verify(certificateDao, times(1)).findByTags(tags, tags.size(), PageRequest.of(0, 5));
        verifyNoMoreInteractions(certificateDao);
    }

    @Test
    void getById() {
        Certificate certificate = new Certificate();
        certificate.setId(3);
        certificate.setName("Certificate3");
        when(certificateDao.findById(3)).thenReturn(Optional.of(certificate));

        Certificate result = certificateService.getById(3);

        assertEquals(certificate, result);

        verify(certificateDao, times(1)).findById(3);
        verifyNoMoreInteractions(certificateDao);
    }

    @Test
    void create() {
        List<Tag> tags = new ArrayList<>();
        Tag tag1 = new Tag();
        tag1.setId(1);
        tag1.setName("Tag1");
        Tag tag2 = new Tag();
        tag2.setName("Tag2");
        tags.add(tag1);
        tags.add(tag2);

        Certificate certificate = new Certificate();
        certificate.setName("METRO");
        certificate.setDescription("supermarket");
        certificate.setTags(new ArrayList<>(tags));

        when(certificateDao.save(certificate)).thenReturn(1);
        when(tagDao.findByName("Tag1")).thenReturn(tag1);
        when(tagDao.findByName("Tag2")).thenReturn(null);
        assertEquals(true, certificateService.create(certificate));
        verify(certificateDao, times(1)).save(any(Certificate.class));
        verify(tagDao, times(2)).findByName(any(String.class));
        verifyNoMoreInteractions(certificateDao);
    }

    @Test
    void update() {
        List<Tag> tags = new ArrayList<>();
        Tag tag1 = new Tag();
        tag1.setId(1);
        tag1.setName("Tag1");
        Tag tag2 = new Tag();
        tag2.setName("Tag2");
        tags.add(tag1);
        tags.add(tag2);

        Certificate certificateDb = new Certificate();
        certificateDb.setId(3);
        certificateDb.setName("METRO");
        certificateDb.setDescription("supermarket");
        certificateDb.setTags(new ArrayList<>());
        when(certificateDao.findById(3)).thenReturn(Optional.of(certificateDb));
        when(tagDao.findByName("Tag1")).thenReturn(tag1);
        when(tagDao.findByName("Tag2")).thenReturn(null);

        Certificate certificate = new Certificate();
        certificate.setId(3);
        certificate.setName("updated");
        certificate.setDescription("new description");
        certificate.setTags(tags);
        when(certificateDao.save(certificate)).thenReturn(3);

        assertEquals(true, certificateService.update(certificate));
        verify(certificateDao, times(1)).save(any(Certificate.class));
        verify(certificateDao, times(1)).findById(3);
        verify(tagDao, times(2)).findByName(any(String.class));
        verifyNoMoreInteractions(certificateDao);
    }

    @Test
    void delete() {
        Certificate certificate = new Certificate();
        certificate.setId(1);
        certificate.setName("Certificate3");
        when(certificateDao.findById(1)).thenReturn(Optional.of(certificate));
        assertEquals(true, certificateService.delete(1));
        verify(certificateDao, times(1)).delete(1);
        verify(certificateDao, times(1)).findById(1);
        verifyNoMoreInteractions(certificateDao);
    }
}