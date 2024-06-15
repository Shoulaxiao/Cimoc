package com.haleydu.cimoc.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @auther shoulaxiao
 * @date 2024/6/15 14:04
 **/
@Entity
public class SearchHistory {
    @Id
    private Integer id;

    @NotNull
    private String keyword;

    @NotNull
    private Long createTime;

    @Generated(hash = 2140492056)
    public SearchHistory(Integer id, @NotNull String keyword,
            @NotNull Long createTime) {
        this.id = id;
        this.keyword = keyword;
        this.createTime = createTime;
    }

    @Generated(hash = 1905904755)
    public SearchHistory() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }


}
