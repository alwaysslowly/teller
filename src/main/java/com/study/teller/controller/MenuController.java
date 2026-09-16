package com.study.teller.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.teller.common.ApiResponse;
import com.study.teller.common.SessionUtil;
import com.study.teller.mapper.MenuMapper;
import com.study.teller.vo.EmpVo;
import com.study.teller.vo.MenuVo;

@RestController
public class MenuController {

    @Autowired
    private MenuMapper menuMapper;

    @GetMapping("/menu")
    public ApiResponse menuList() {

        EmpVo emp = SessionUtil.getEmp();
        List<MenuVo> list = menuMapper.selectMenuList(emp.getAuthLv());

        return ApiResponse.ok(list);
    }
}