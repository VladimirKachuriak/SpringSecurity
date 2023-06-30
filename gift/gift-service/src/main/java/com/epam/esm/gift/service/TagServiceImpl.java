package com.epam.esm.gift.service;

import com.epam.esm.gift.model.Tag;
import com.epam.esm.gift.model.repo.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService{
    private final TagDao tagDao;
    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }
    @Override
    public List<Tag> getAll() {
        return tagDao.findAll();
    }
    @Override
    public List<Tag> getAll(int page) {
        List<Tag> tags;
        if (page < 1) {
            page = 1;
        }
        page--;
        tags = tagDao.findAll(PageRequest.of(page, 5)).stream().toList();
        return tags;
    }
    @Override
    public Tag getById(int id) {
        return tagDao.findById(id).orElse(null);
    }

    @Override
    public boolean create(Tag tag) {
        Tag tagDB = tagDao.findByName(tag.getName());
        if (tagDB != null) return false;
        tagDao.save(tag);
        return true;
    }
    @Override
    public boolean update(Tag tag) {
        Tag tagDB = tagDao.findById(tag.getId()).orElse(null);
        if (tagDB == null || tagDao.findByName(tag.getName()) != null) return false;
        tagDB.setName(tag.getName());
        tagDao.save(tagDB);
        return true;
    }
    @Override
    public boolean delete(int id) {
        Tag tagDB = tagDao.findById(id).orElse(null);
        if (tagDB == null) return false;
        tagDao.delete(tagDB);
        return true;
    }
}
