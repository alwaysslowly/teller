package com.study.teller.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.teller.common.ApiResponse;
import com.study.teller.mapper.CommCodeMapper;
import com.study.teller.vo.CommCodeVo;

@RestController
public class CommCodeController {

    @Autowired
    private CommCodeMapper commCodeMapper;

    @GetMapping("/code")
    public ApiResponse codeList(@RequestParam String grpCode) {
        List<CommCodeVo> list = commCodeMapper.selectCodeList(grpCode);
        return ApiResponse.ok(list);
    }
}