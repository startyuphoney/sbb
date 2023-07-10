package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Answer.Answer;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
	public Page<Question> getList(int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Specification<Question> spec = search(kw);
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
	public void create(String subeject, String content, SiteUser siteUser) {
		Question q = new Question();
		q.setSubject(subeject);
		q.setContent(content);
		q.setCreateDate(LocalDateTime.now());
		q.setAuthor(siteUser);
		this.questionRepository.save(q);
	}
	
	// 질문 수정
	public void modify(Question question, String subject, String content) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		this.questionRepository.save(question);
	}
	
	// 질문 삭제
	public void delete(Question question) {
		this.questionRepository.delete(question);
	}
	
	// 투표
	public void vote(Question question, SiteUser siteUser) {
		question.getVoter().add(siteUser);
		this.questionRepository.save(question);
		}
	
	// 검색
	private Specification<Question> search(String kw){
		return new Specification<>() {
		private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true); // 중복 제거
				Join<Question, SiteUser> u1 = q.join("author",JoinType.LEFT);
				Join<Question, Answer> a = q.join("answerList",JoinType.LEFT);
				Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
				return cb.or(cb.like(q.get("subject"),"%" + kw + "%"), // 제목
						cb.like(q.get("content"), "%" + kw + "%"),
						cb.like(u1.get("username"), "%" + kw + "%"),
						cb.like(a.get("content"), "%" + kw + "%"),
						cb.like(u2.get("username"), "%" + kw + "%"));
			}
		};
	}
	
}
