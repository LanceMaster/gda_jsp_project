package model.dto;

import java.util.Date;

public class CommentDTO {
    private int comment_id;
    private String content;
    private Integer parent_id;
    private boolean is_deleted;
    private Date created_at;
    private int project_id;
    private int user_id;

    // Getter / Setter - CamelCase 규칙 적용
    public int getCommentId() { return comment_id; }
    public void setCommentId(int comment_id) { this.comment_id = comment_id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getParentId() { return parent_id; }
    public void setParentId(Integer parent_id) { this.parent_id = parent_id; }

    public boolean isDeleted() { return is_deleted; }
    public void setDeleted(boolean is_deleted) { this.is_deleted = is_deleted; }

    public Date getCreatedAt() { return created_at; }
    public void setCreatedAt(Date created_at) { this.created_at = created_at; }

    public int getProjectId() { return project_id; }
    public void setProjectId(int project_id) { this.project_id = project_id; }

    public int getUserId() { return user_id; }
    public void setUserId(int user_id) { this.user_id = user_id; }
}
