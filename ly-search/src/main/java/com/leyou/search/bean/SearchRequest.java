package com.leyou.search.bean;

import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * @author Srd
 * @date 2020/7/24  0:41
 */
public class SearchRequest {

    private String key;
    private Integer page;

    private String sortBy;
    private Boolean descending;

    private static final Integer DEFAULT_SIZE = 20;
    private static final Integer DEFAULT_PAGE = 1;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if (page == null){
            return DEFAULT_PAGE;
        }
        return Math.max(DEFAULT_PAGE,page);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public static Integer getSize() {
        return DEFAULT_SIZE;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getDescending() {
        return descending;
    }

    public void setDescending(Boolean descending) {
        this.descending = descending;
    }

    @Override
    public String toString() {
        return "SearchRequest{" +
                "key='" + key + '\'' +
                ", page=" + page +
                ", sortBy='" + sortBy + '\'' +
                ", descending=" + descending +
                '}';
    }
}
