package com.app.sample.compose.data

import com.app.sample.compose.net.SampleApiResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WanAndroidApi {

    @GET("banner/json")
    suspend fun loadBanners(): SampleApiResult<List<Banner>>

    @GET("article/list/{page}/json")
    suspend fun loadHomeArticles(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int,
    ): SampleApiResult<WanAndroidPager<Article>>

    @GET("user_article/list/{pageNo}/json")
    suspend fun loadSquareArticles(
        @Path("pageNo") pageNo: Int,
        @Query("page_size") pageSize: Int,
    ): SampleApiResult<WanAndroidPager<Article>>

}