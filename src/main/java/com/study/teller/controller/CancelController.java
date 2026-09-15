package com.study.teller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.teller.common.ApiResponse;
import com.study.teller.service.CancelService;
import com.study.teller.vo.CancelReqVo;

@RestController
public class CancelController {

    @Autowired
    private CancelService cancelService;

    @PostMapping("/cancel")
    public ApiResponse cancel(@RequestBody CancelReqVo vo) throws Exception {
        return ApiResponse.ok(cancelService.cancel(vo));
    }
}