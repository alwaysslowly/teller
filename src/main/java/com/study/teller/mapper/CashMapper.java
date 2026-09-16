package com.study.teller.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.study.teller.vo.CashVo;

@Mapper
public interface CashMapper {

    /** 시재 정보 조회 */
    CashVo selectCash(CashVo param);

    /** 당일 거래 집계 (입금/출금 합계) */
    CashVo selectTrSum(CashVo param);

    /** 마감 처리 */
    int updateClose(CashVo param);
    
    /** 영업점 직원들의 마감 현황 */
    List<CashVo> selectBranchCashList(CashVo param);

    /** 영업일 마감 처리 */
    int updateBizClose(String baseDate);
    
}