package me.ztiany.wan.main.data

import com.app.base.data.protocol.ApiResult
import com.app.common.api.network.Pager
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeApi {

    @GET("banner/json")
    suspend fun loadBanners(): ApiResult<List<Banner>>

    @GET("article/top/json")
    suspend fun loadTopArticles(): ApiResult<List<Article>>

    @GET("article/list/{page}/json")
    suspend fun loadHomeArticles(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int,
    ): ApiResult<Pager<Article>>

    @GET("user_article/list/{pageNo}/json")
    suspend fun loadSquareArticles(
        @Path("pageNo") pageNo: Int,
        @Query("page_size") pageSize: Int,
    ): ApiResult<Pager<Article>>

}