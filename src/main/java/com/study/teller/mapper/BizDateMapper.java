package com.study.teller.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.study.teller.vo.BizDateVo;

@Mapper
public interface BizDateMapper {

    /** 해당 일자 정보 */
    BizDateVo selectBizDate(String baseDate);

    /** 기준일 이후 첫 영업일 */
    String selectNextBizDate(String baseDate);
}