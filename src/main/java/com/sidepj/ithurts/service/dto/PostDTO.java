package com.sidepj.ithurts.service.dto;

import com.sidepj.ithurts.domain.Comment;
import com.sidepj.ithurts.domain.Post;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDTO {

    private Long id;

    private MemberDTO memberDTO;

    private String name;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private String postType;

    public PostDTO(Post post){
        this.id = post.getId();
        this.name = post.getPostName();
        this.content = post.getContent();
        this.createdDate = post.getCreatedDate();
        this.memberDTO = new MemberDTO(post.getPostMember());
    }

}
