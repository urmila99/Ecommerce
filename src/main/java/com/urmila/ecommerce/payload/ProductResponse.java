package com.urmila.ecommerce.payload;

import java.util.List;

public class ProductResponse {

    private List<ProductDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalElements;
    private Integer totalPages;
    private boolean lastPage;

    public List<ProductDTO> getContent() {
        return content;
    }

    public void setContent(List<ProductDTO> content) {
        this.content = content;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }
}
