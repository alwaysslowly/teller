package com.study.teller.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.study.teller.vo.TrHistVo;

@Mapper
public interface TrHistMapper {

    int insertHist(TrHistVo vo);

    TrHistVo selectHist(String trNo);
}