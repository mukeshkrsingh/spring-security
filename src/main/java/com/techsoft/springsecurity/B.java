package com.techsoft.springsecurity;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class B implements Test {
	
	@Override
    public String performAction() {
        return "Action performed by B";
    }
	
}
