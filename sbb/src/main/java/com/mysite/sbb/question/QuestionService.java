package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {
	
	private final QuestionRepository questionRepository;
	
	// 모든 질문 목록 조회
	public List<Question> getList() {
		return this.questionRepository.findAll();
	}
	
	// 질문 목록 조회 페이징
	public Page<Question> getList(int page) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.questionRepository.findAll(pageable);
	}
	
	// 질문상세보기 위한 단일질문 반환
	public Question getQuestion(Integer id) {
		Optional<Question> question = this.questionRepository.findById(id);
				if (question.isPresent()) {
					return question.get();
				} else {
					throw new DataNotFoundException("question not found");
				}
	}
	
	// 질문 생성
	public void create(String subeject, String content) {
		Question q = new Question();
		q.setSubject(subeject);
		q.setContent(content);
		q.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q);
	}
	
	
}
