package com.nvest.user.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {
    @SerializedName("Product")
    @Expose
    private List<ProductContents> product = null;

    public List<ProductContents> getProduct() {
        return product;
    }

    public void setProduct(List<ProductContents> product) {
        this.product = product;
    }
}
