package com.kh.start.board.model.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Board {
    private Long boardNo;
    private String boardTitle;
    private String boardContent;
    private String boardWriter;       // 추가
    private String boardFileUrl;    // 추가
}