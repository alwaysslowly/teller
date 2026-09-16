package com.study.teller.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.study.teller.vo.MenuVo;

@Mapper
public interface MenuMapper {

    /** 권한에 맞는 메뉴 목록 */
    List<MenuVo> selectMenuList(String authLv);
}