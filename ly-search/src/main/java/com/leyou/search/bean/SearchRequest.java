package com.leyou.search.bean;

/**
 * @author Srd
 * @date 2020/7/24  0:41
 */
public class SearchRequest {

    private String key;
    private Integer page;

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
}
