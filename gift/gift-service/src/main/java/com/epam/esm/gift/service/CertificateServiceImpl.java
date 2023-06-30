package com.epam.esm.gift.service;

import com.epam.esm.gift.model.Certificate;
import com.epam.esm.gift.model.Tag;
import com.epam.esm.gift.model.repo.CertificateDao;
import com.epam.esm.gift.model.repo.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateDao certificateDAO;
    private final TagDao tagDao;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao, TagDao tagDao) {
        this.certificateDAO = certificateDao;
        this.tagDao = tagDao;
    }

    @Override
    public List<Certificate> getAll() {
        List<Certificate> certificates;
        certificates = certificateDAO.findAll();
        return certificates;
    }

    @Override
    public List<Certificate> getAll(int page, List<String> tags) {
        List<Certificate> certificates;
        if (page < 1) {
            page = 1;
        }
        page--;
        certificates = certificateDAO.findByTags(tags, tags.size(), PageRequest.of(page, 5));
        return certificates;
    }

    @Override
    public Certificate getById(int id) {
        Certificate certificate = certificateDAO.findById(id).orElse(null);
        return certificate;
    }

    @Override
    public boolean create(Certificate certificate) {
        List<Tag> tags = new ArrayList<>();
        for (Tag tag : certificate.getTags()) {
            Tag tagDb = tagDao.findByName(tag.getName());
            if (tagDb == null) {
                tags.add(tag);
            } else {
                tags.add(tagDb);
            }
        }
        certificate.setTags(tags);
        certificateDAO.save(certificate);
        return true;
    }

    @Override
    public boolean update(Certificate certificate) {
        Certificate certificateDb = certificateDAO.findById(certificate.getId()).orElse(null);
        if(certificateDb == null || certificate.equals(certificateDb))return false;
        certificateDb.setLastUpdateDate(LocalDateTime.now());
        if (certificate.getName() != null) certificateDb.setName(certificate.getName());
        if (certificate.getDescription() != null) certificateDb.setDescription(certificate.getDescription());
        if (certificate.getPrice() != certificateDb.getPrice()) certificateDb.setPrice(certificate.getPrice());
        if (certificate.getDuration() != certificateDb.getDuration())
            certificateDb.setDuration(certificate.getDuration());
        if (certificate.getTags() !=null || !certificateDb.getTags().equals(certificate.getTags())) {
            List<Tag> tags = new ArrayList<>();
            for (Tag tag : certificate.getTags()) {
                Tag tagDb = tagDao.findByName(tag.getName());
                if (tagDb == null) {
                    tags.add(tag);
                } else {
                    tags.add(tagDb);
                }
            }
            certificateDb.setTags(tags);
        }
        certificateDAO.save(certificateDb);
        return true;
    }

    @Override
    public boolean delete(int id) {
        Certificate certificateDB = certificateDAO.findById(id).orElse(null);
        if (certificateDB == null) return false;
        certificateDAO.delete(certificateDB);
        return true;
    }
}
