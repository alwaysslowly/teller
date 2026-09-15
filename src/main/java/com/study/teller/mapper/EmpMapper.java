package com.study.teller.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.study.teller.vo.EmpVo;

@Mapper
public interface EmpMapper {

    EmpVo selectEmp(String empNo);
}