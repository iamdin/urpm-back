package com.example.urpm.service;

import org.springframework.stereotype.Service;

import java.util.List;

public interface BaseService<T> {

    List<T> selectAll();

    T selectByPrimaryKey(Object o);

    T selectOne(T t);

    int insert(T t);

    int updateByPrimaryKeySelective(T t);

    int deleteByPrimaryKey(Object o);
}
