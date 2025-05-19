// 📁 model.dto.LectureSearchCondition.java
package model.dto;

public class LectureSearchCondition {
    private String category;
    private String keyword;
    private String sort = "latest";
    private int page = 1;
    private int size = 8;

    public int getOffset() {
        return (page - 1) * size;
    }

	public String getCategory() {
		
		return category;
	}
	
	public void setCategory(String category) {
	    this.category = (category != null && !category.trim().isEmpty()) ? category : null;
	}


	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
