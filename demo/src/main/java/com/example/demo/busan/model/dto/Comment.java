package com.example.demo.busan.model.dto;

import java.sql.Date;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Service
@ToString
public class Comment {
	private Long seq;
	private String content;
	private Date createDate;
}
