package com.nvest.user.home;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {
    //@GET("GetCompanyDefinedCatProductsFull?CompanyId=1")
    @GET("1h5jxm/")
    Call<Product> doGetUserList();

    @GET("rjwx6/")
    Call<Product> getAdditionalProductsList();
}
