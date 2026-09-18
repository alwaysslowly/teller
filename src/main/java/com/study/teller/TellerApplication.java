package com.study.teller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.study.teller.common.PasswordUtil;
import com.study.teller.mapper.EmpMapper;

@SpringBootApplication
public class TellerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TellerApplication.class, args);
    }

    /** 시작 시 직원 비밀번호를 해시로 변환 (학습용) */
    @Bean
    ApplicationRunner initPassword(EmpMapper empMapper) {
        return args -> {
            String[] empNos = { "E12345", "E99999", "E54321" };
            for (String empNo : empNos) {
                empMapper.updatePasswd(empNo, PasswordUtil.encode("1234"));
            }
            System.out.println(">> 직원 비밀번호 해시 변환 완료");
        };
    }
}