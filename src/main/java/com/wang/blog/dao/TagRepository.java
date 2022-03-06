package com.wang.blog.dao;

import com.wang.blog.pojo.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Long> {
    Tag getTagByName(String name);
}
