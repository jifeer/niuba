package com.etyero.dao;

import java.util.List;

import com.etyero.entity.Code;

public interface CodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Code code);

    int insertSelective(Code code);

    List<Code> selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Code code);

}
