package model.dto;

/**
 * 🏷️ TagDTO
 * - 태그 정보 전송 객체
 */
public class TagDTO {

    private int tagId;
    private String name;

    
    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
