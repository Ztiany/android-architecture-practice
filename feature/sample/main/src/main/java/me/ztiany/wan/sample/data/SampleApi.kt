package me.ztiany.wan.sample.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SampleApi {

    @GET("banner/json")
    suspend fun loadBanners(): SampleApiResult<List<Banner>>

    @GET("article/top/json")
    suspend fun loadTopArticles(): SampleApiResult<List<Article>>

    @GET("article/list/{page}/json")
    suspend fun loadHomeArticles(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int,
    ): SampleApiResult<Pager<Article>>

    @GET("user_article/list/{pageNo}/json")
    suspend fun loadSquareArticles(
        @Path("pageNo") pageNo: Int,
        @Query("page_size") pageSize: Int,
    ): SampleApiResult<Pager<Article>>

}