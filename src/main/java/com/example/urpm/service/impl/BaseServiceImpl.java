package com.example.urpm.service.impl;

import com.example.urpm.service.BaseService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dingjinyang
 * @datetime 2020/2/11 21:47
 * @description BaseService implements
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {


    private Mapper<T> mapper;

    @Autowired
    public void setMapper(Mapper<T> mapper) {
        this.mapper = mapper;
    }

    public Mapper<T> getMapper() {
        return mapper;
    }

    /**
     * 查询所有数据
     * @return List
     */
    @Override
    public List<T> selectAll() {
        return mapper.selectAll();
    }

    /**
     * 根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号
     * @param key Object
     * @return T
     */
    @Override
    public T selectByPrimaryKey(Object key) {
        //说明：
        return mapper.selectByPrimaryKey(key);
    }

    @Override
    public T selectOne(T record) {
        return mapper.selectOne(record);
    }

    @Override
    public int insert(T record) {
        return mapper.insert(record);
    }

    @Override
    public int updateByPrimaryKeySelective(T t) {
        return mapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public int deleteByPrimaryKey(Object o){
        return mapper.deleteByPrimaryKey(o);
    }

}
