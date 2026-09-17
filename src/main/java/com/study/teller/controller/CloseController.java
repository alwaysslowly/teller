package com.study.teller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.teller.common.ApiResponse;
import com.study.teller.service.CloseService;
import com.study.teller.vo.CloseReqVo;

@RestController
public class CloseController {

    @Autowired
    private CloseService closeService;

    @PostMapping("/close")
    public ApiResponse close(@RequestBody CloseReqVo vo) throws Exception {
        return ApiResponse.ok(closeService.close(vo));
    }

    @PostMapping("/close/cancel")
    public ApiResponse cancelClose(@RequestBody CloseReqVo vo) throws Exception {
        return ApiResponse.ok(closeService.cancelClose(vo));
    }
}