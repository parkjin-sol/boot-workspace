package com.kh.start.comments.model.vo;


import java.sql.Date;

import lombok.Builder;
@Builder
public class Comment {
	private Long commentNo;
	private String commentContent;
	private Long commentWriter;
	private Date createDate;
	private Long refBoardNo;
}
