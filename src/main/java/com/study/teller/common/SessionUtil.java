package com.study.teller.common;

import com.study.teller.vo.EmpVo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionUtil {

    /** 로그인한 직원 정보 (없으면 예외) */
    public static EmpVo getEmp() {

        ServletRequestAttributes attr =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attr == null) {
            throw new BizException("L003", "로그인이 필요합니다.");
        }

        HttpServletRequest request = attr.getRequest();
        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new BizException("L003", "로그인이 필요합니다.");
        }

        EmpVo emp = (EmpVo) session.getAttribute("EMP");

        if (emp == null) {
            throw new BizException("L003", "로그인이 필요합니다.");
        }

        return emp;
    }
    
    /** 책임자 권한 확인 */
    public static void checkManager() {
        EmpVo emp = getEmp();
        if (!AuthLevel.MANAGER.equals(emp.getAuthLv())) {
            throw new BizException("A001", "책임자 권한이 필요합니다.");
        }
    }
    
}