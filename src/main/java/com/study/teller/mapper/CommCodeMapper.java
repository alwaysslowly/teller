package com.study.teller.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.study.teller.vo.CommCodeVo;

@Mapper
public interface CommCodeMapper {

    List<CommCodeVo> selectCodeList(String grpCode);

    String selectCodeNm(CommCodeVo vo);
}