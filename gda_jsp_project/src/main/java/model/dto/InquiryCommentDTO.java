package model.dto;

import java.util.Date;

public class InquiryCommentDTO {
    private int comment_id;
    private String content;
    private boolean is_deleted;
    private Date created_at;
    private int inquiry_id;
    private int user_id;
    private String userName;

    public int getCommentId() { return comment_id; }
    public void setCommentId(int comment_id) { this.comment_id = comment_id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public boolean isDeleted() { return is_deleted; }
    public void setDeleted(boolean is_deleted) { this.is_deleted = is_deleted; }

    public Date getCreatedAt() { return created_at; }
    public void setCreatedAt(Date created_at) { this.created_at = created_at; }

    public int getInquiryId() { return inquiry_id; }
    public void setInquiryId(int inquiry_id) { this.inquiry_id = inquiry_id; }

    public int getUserId() { return user_id; }
    public void setUserId(int user_id) { this.user_id = user_id; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}
