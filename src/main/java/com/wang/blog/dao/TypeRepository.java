package com.wang.blog.dao;

import com.wang.blog.pojo.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<Type,Long> {
    Type getTypeByName(String name);

}
