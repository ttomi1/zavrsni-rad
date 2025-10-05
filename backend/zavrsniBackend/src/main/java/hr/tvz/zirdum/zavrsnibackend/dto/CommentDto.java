package hr.tvz.zirdum.zavrsnibackend.dto;

import hr.tvz.zirdum.zavrsnibackend.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private String authorUsername;
    private String authorImageUrl;
    private LocalDateTime createdAt;

    public static CommentDto from(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthorUsername(comment.getAuthor().getUserName());
        dto.setAuthorImageUrl(comment.getAuthor().getImageUrl());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}
