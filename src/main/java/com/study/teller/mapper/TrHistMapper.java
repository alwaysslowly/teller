package com.study.teller.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.study.teller.vo.TrHistVo;

@Mapper
public interface TrHistMapper {

    int insertHist(TrHistVo vo);
    List<TrHistVo> selectHistList(String acctNo);

    TrHistVo selectHist(String trNo);
    
}