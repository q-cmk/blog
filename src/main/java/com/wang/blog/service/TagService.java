package com.wang.blog.service;

import com.wang.blog.pojo.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    //增加一个Tag
    Tag saveTag(Tag tag);

    //根据id删除一个Tag
    void delete(Long id);

    //更新Tag
    Tag updateTag(Long id,Tag tag);

    //查询Tag 列表
    List<Tag> listTag();

    List<Tag> listTag(String ids);

    //查询Tag Page
    Page<Tag> listTag(Pageable pageable);

    //根据id获得一个Tag
    Tag getTag(Long id);

    //根据名字查找Tag
    Tag getTagByName(String name);
}
