package com.example.demo.busan.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.busan.model.dto.Comment;
import com.example.demo.busan.model.service.BusanService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
// 리액트한테 응답 보여줄때 json 형태로 해야함
// 따라서 이제 얘는 순수하게 데이터만 쏴주는 API 서버가됨
@RestController
@CrossOrigin(origins="*")
@RequestMapping("busans")
@RequiredArgsConstructor // private final BusanService busanService;
@Slf4j
public class BusanController {
	private final BusanService busanService;
	
	@GetMapping
	public ResponseEntity<String> getBusanFoods(@RequestParam(name="pageNo", defaultValue="1")int pageNo) {

		String responseData = busanService.requestGetBusan(pageNo);
		return ResponseEntity.ok(responseData); // responseData => 응답하고 싶은 데이터
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<String> requestGetBusanDetail(@PathVariable(name="id") int id) {
		String response = busanService.requestGetBusanDetail(id);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/comments")
	public ResponseEntity<?> save(@RequestBody Comment comment) {
		busanService.saveComment(comment);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/comments/{id}")
	public ResponseEntity<List<Comment>> getComment(@PathVariable(name="id") Long id) {
		List<Comment> comments = busanService.selectCommentList(id);
		return ResponseEntity.ok(comments);
	}

	
	
}