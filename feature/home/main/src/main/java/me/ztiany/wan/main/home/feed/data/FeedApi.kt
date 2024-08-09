package me.ztiany.wan.main.home.feed.data

import com.app.base.data.protocol.ApiResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FeedApi {

    @GET("banner/json")
    suspend fun loadBanners(): ApiResult<List<Banner>>

    @GET("article/top/json")
    suspend fun loadTopArticles(): ApiResult<List<Article>>

    @GET("article/list/{page}/json")
    suspend fun loadHomeArticles(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int,
    ): ApiResult<Pager<Article>>

}