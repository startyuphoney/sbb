package com.mysite.sbb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.question.QuestionService;

import jakarta.transaction.Transactional;

@SpringBootTest
class SbbApplicationTests {
	
	@Autowired
	private QuestionRepository questionRepository; 
	
	@Autowired
	private QuestionService questionService;
	
	@Test
	void testJpa() {
		for(int i = 0; i < 300; i++) {
			String subject = "테스트입니다! [" + i + "]";
			String content = "내용입니다";
//			this.questionService.create(subject, content);
		}
	}
}
