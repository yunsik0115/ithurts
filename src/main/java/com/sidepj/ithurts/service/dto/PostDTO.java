package com.sidepj.ithurts.service.dto;

import com.sidepj.ithurts.domain.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDTO {

    private Long id;

    private MemberControllerDTO memberControllerDTO;

    private String name;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private String postType;

    private int commentCount;

    private int loveCount;

    public PostDTO(Post post){
        this.id = post.getId();
        this.name = post.getPostName();
        this.content = post.getContent();
        this.createdDate = post.getCreatedDate();
        this.memberControllerDTO = new MemberControllerDTO(post.getPostMember());
        this.commentCount = post.getComments().size();
        this.loveCount = post.getLoves().size();
    }

}
