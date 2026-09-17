package com.study.teller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.teller.common.ApiResponse;
import com.study.teller.service.NewAcctService;
import com.study.teller.vo.NewAcctReqVo;

@RestController
public class NewAcctController {

    @Autowired
    private NewAcctService newAcctService;

    @PostMapping("/newacct")
    public ApiResponse newAcct(@RequestBody NewAcctReqVo vo) throws Exception {
        return ApiResponse.ok(newAcctService.newAcct(vo));
    }
}