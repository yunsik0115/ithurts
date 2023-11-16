package com.sidepj.ithurts.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentDTO {

    private Long postId;

    private String content;

    private Long memberId;


}
