package com.techsoft.springsecurity.controller;

import java.util.List;
import java.util.Objects;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techsoft.springsecurity.Test;
import com.techsoft.springsecurity.entity.AuthRequest;
import com.techsoft.springsecurity.entity.UserInfo;
import com.techsoft.springsecurity.logout.BlackList;
import com.techsoft.springsecurity.service.JwtService;
import com.techsoft.springsecurity.service.UserInfoService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private BlackList blackList;
    
    @Autowired
    private final Test testObj;
    
   
    public UserController(Test testObj) {
        this.testObj = testObj;
    }
   
   
    
    @GetMapping("/welcome")
    public String welcome(){
    	
        return    testObj.performAction() +  " ok Welcome to Spring Security tutorials !!";
    }

    @PostMapping("/addUser")
    public String addUser(@RequestBody UserInfo userInfo){
        return userInfoService.addUser(userInfo);
    }
    @PostMapping("/login")
    public String addUser(@RequestBody AuthRequest authRequest){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        if(authenticate.isAuthenticated()){
            return jwtService.generateToken(authRequest.getUserName());
        }else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }
    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('USER_ROLES') or hasAuthority('ADMIN_ROLES')")
    public String logoutUser(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        String token= null;
        if(authHeader !=null && authHeader.startsWith("Bearer")){
            token = authHeader.substring(7);
        }
        blackList.blacKListToken(token);
        return "You have successfully logged out !!";
    }

    @GetMapping("/getUsers")
    @PreAuthorize("hasAuthority('ADMIN_ROLES') or hasAuthority('USER_ROLES')")
    public List<UserInfo> getAllUsers(){
        return userInfoService.getAllUser();
    }
    @GetMapping("/getUsers/{id}")
    @PreAuthorize("hasAuthority('USER_ROLES')")
    public UserInfo getAllUsers(@PathVariable Integer id){
        return userInfoService.getUser(id);
    }
    
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN_ROLES')")
    public String update(@RequestBody UserInfo info) {
    	
    		 UserInfo i =  userInfoService.getUserByName(info.getName());
    		 if (Objects.nonNull(info.getName().toLowerCase())
    		            && !"".equalsIgnoreCase(
    		                i.getName().toLowerCase())) {
    			 i.setName(info.getName().toLowerCase());
    			 i.setPassword(info.getPassword());
    			 userInfoService.addUser(i);
    			 return "User updated successfully";
    		        }else {
    		        	 throw new UsernameNotFoundException("Invalid user request");
    		        }
             
         
    	
    	
    }
    
    
    
}
