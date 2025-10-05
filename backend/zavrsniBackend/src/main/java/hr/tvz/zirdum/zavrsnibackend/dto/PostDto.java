package hr.tvz.zirdum.zavrsnibackend.dto;

import hr.tvz.zirdum.zavrsnibackend.model.Post;

import java.time.LocalDateTime;

public record PostDto(
        Long id,
        String text,
        String imageUrl,
        LocalDateTime createdAt,
        String authorUsername,
        String authorImageUrl
) {
    public static PostDto from(Post post) {
        System.out.println("Post: " + post.getText() + " → " + post.getAuthor());
        return new PostDto(
                post.getId(),
                post.getText(),
                post.getImageUrl(),
                post.getCreatedAt(),
                post.getAuthor().getUserName(),
                post.getAuthor().getImageUrl()
        );
    }
}