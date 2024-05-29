package com.techsoft.springsecurity;

import org.springframework.stereotype.Component;

@Component
public class A implements Test {
	
	@Override
    public String performAction() {
        return "Action performed by A";
    }
	

}
