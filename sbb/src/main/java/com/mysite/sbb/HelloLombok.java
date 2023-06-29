package com.mysite.sbb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor // 이건 생성자 만들어줌 
public class HelloLombok {

	private final String hello;
	private final int lombok;
	
	public static void main(String[] args) {
		HelloLombok helloLombok = new HelloLombok("gg",2);
		
		System.out.println(helloLombok.getHello());
		System.out.println(helloLombok.getLombok());
		
	}

}
