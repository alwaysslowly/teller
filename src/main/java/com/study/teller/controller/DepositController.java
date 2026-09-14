package com.study.teller.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.teller.service.DepositService;
import com.study.teller.vo.DepositReqVo;
import com.study.teller.vo.DepositResVo;

@RestController
public class DepositController {

    @PostMapping("/deposit")
    public DepositResVo deposit(@RequestBody DepositReqVo vo) throws Exception {

        DepositService service = new DepositService();
        return service.deposit(vo);
    }
}