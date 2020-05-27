package com.my.securitytest.controller;

import com.my.securitytest.model.UserInfo;
import com.my.securitytest.util.SecurityUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/getUser")
    @PreAuthorize("hasAuthority('ssjk')")
    @ApiOperation(value = "获取用户信息")
    public ResponseEntity<String> getUser(){
        UserInfo obj = SecurityUtil.getCurrentUser();
        return new ResponseEntity<>("我是用户信息,id:"+obj.getId()+",name:"+obj.getUsername(), HttpStatus.OK);
    }

    @GetMapping("/getUser2")
    public ResponseEntity<String> getUser2(){
        return new ResponseEntity<>("我是用户信息2", HttpStatus.OK);
    }

}
