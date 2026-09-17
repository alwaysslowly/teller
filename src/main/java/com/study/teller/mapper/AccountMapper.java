package com.study.teller.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.study.teller.vo.AccountVo;

@Mapper
public interface AccountMapper {

    AccountVo selectAccount(String acctNo);
    int updateClose(AccountVo vo);
    int updateBalance(AccountVo vo);
    List<AccountVo> selectAccountList(String bankCode);
    int insertAccount(AccountVo vo);
    String selectMaxAcctNo(String bankCode);
}