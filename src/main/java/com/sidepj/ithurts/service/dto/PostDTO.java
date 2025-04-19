package com.sidepj.ithurts.service.dto;

import com.sidepj.ithurts.domain.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
// == TO - DO == PostDTO 저장용, 화면 표시용 분리
public class PostDTO {

    private Long id;

    private String name;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private String postType;

    private String author;

    private int commentCount;

    private int loveCount;

    public PostDTO(Post post){
        this.id = post.getId();
        this.name = post.getPostName();
        this.content = post.getContent();
        this.createdDate = post.getCreatedDate();
        this.commentCount = post.getComments().size();
        this.loveCount = post.getLoves().size();
        this.author = post.getPostMember().getName();
    }

    public PostDTO(String name, String content) {
        this.name = name;
        this.content = content;
    }
}
