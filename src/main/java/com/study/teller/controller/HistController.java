package com.study.teller.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.teller.common.ApiResponse;
import com.study.teller.mapper.TrHistMapper;
import com.study.teller.vo.TrHistVo;

@RestController
public class HistController {

    @Autowired
    private TrHistMapper trHistMapper;

    @GetMapping("/hist")
    public ApiResponse hist(@RequestParam String acctNo) { 
        List<TrHistVo> list = trHistMapper.selectHistList(acctNo);
        return ApiResponse.ok(list);
    }
}