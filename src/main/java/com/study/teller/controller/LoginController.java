package com.study.teller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.teller.common.ApiResponse;
import com.study.teller.common.SessionUtil;
import com.study.teller.service.LoginService;
import com.study.teller.vo.EmpVo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ApiResponse login(@RequestBody Map<String, String> param,
                             HttpServletRequest request) {

        EmpVo emp = loginService.login(param.get("empNo"), param.get("passwd"));

        // 세션에 저장
        HttpSession session = request.getSession();
        session.setAttribute("EMP", emp);
        session.setMaxInactiveInterval(30 * 60);   // 30분

        return ApiResponse.ok(emp);
    }

    @PostMapping("/logout")
    public ApiResponse logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return ApiResponse.ok(null);
    }
    
    @GetMapping("/session")
    public ApiResponse session() {
        return ApiResponse.ok(SessionUtil.getEmp());
    }
    
    
    
    
    
}