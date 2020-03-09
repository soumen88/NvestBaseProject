package com.nvest.user.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "Categories")
public class TempTable {
    @org.greenrobot.greendao.annotation.Id (autoincrement = true)
    private Long categoryId;

    private Long categoryIdReceived;

    private String name;

    @Generated(hash = 1832113316)
    public TempTable(Long categoryId, Long categoryIdReceived, String name) {
        this.categoryId = categoryId;
        this.categoryIdReceived = categoryIdReceived;
        this.name = name;
    }

    @Generated(hash = 624089156)
    public TempTable() {
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategoryIdReceived() {
        return categoryIdReceived;
    }

    public void setCategoryIdReceived(Long categoryIdReceived) {
        this.categoryIdReceived = categoryIdReceived;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
