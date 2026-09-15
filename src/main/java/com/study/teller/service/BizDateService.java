package com.study.teller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.teller.common.BizException;
import com.study.teller.common.DateUtil;
import com.study.teller.mapper.BizDateMapper;
import com.study.teller.vo.BizDateVo;

@Service
public class BizDateService {

    @Autowired
    private BizDateMapper bizDateMapper;

    /** 거래에 적용할 영업일을 구한다 */
    public String getBizDate() {

        String today = DateUtil.getToday();
        BizDateVo info = bizDateMapper.selectBizDate(today);

        // 달력에 없는 날 → 영업일 테이블 관리 누락
        if (info == null) {
            throw new BizException("D001", "영업일 정보가 없습니다. 관리자에게 문의하세요.");
        }

        // 영업일이 아니거나(주말·공휴일) 이미 마감했으면 → 다음 영업일
        if (!"Y".equals(info.getBizYn()) || "Y".equals(info.getCloseYn())) {
            String next = bizDateMapper.selectNextBizDate(today);
            if (next == null) {
                throw new BizException("D002", "다음 영업일 정보가 없습니다.");
            }
            return next;
        }

        return today;
    }
}