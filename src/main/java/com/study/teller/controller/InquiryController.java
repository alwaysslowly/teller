package com.study.teller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.teller.common.ApiResponse;
import com.study.teller.service.InquiryService;
import com.study.teller.vo.InquiryReqVo;

@RestController
public class InquiryController {

    @Autowired
    private InquiryService inquiryService;

    @PostMapping("/inquiry")
    public ApiResponse inquiry(@RequestBody InquiryReqVo vo) throws Exception {
        return ApiResponse.ok(inquiryService.inquiry(vo));
    }
}