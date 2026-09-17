package com.study.teller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.teller.common.ApiResponse;
import com.study.teller.service.TransferService;
import com.study.teller.vo.TransferReqVo;

@RestController
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping("/transfer")
    public ApiResponse transfer(@RequestBody TransferReqVo vo) throws Exception {
        return ApiResponse.ok(transferService.transfer(vo));
    }
}