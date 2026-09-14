package com.study.teller.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.teller.common.ApiResponse;
import com.study.teller.service.HistoryService;
import com.study.teller.vo.HistoryReqVo;
import com.study.teller.vo.HistoryResVo;

@RestController
public class HistoryController {

    @PostMapping("/history")
    public ApiResponse history(@RequestBody HistoryReqVo vo) throws Exception {

        HistoryService service = new HistoryService();
        return ApiResponse.ok(service.history(vo));

    }
}