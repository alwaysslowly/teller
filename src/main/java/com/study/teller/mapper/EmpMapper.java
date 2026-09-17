package com.study.teller.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.study.teller.vo.EmpVo;

@Mapper
public interface EmpMapper {

    EmpVo selectEmp(String empNo);
    int updatePasswd(@Param("empNo") String empNo, @Param("passwd") String passwd);
}