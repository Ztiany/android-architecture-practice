package me.ztiany.wan.main.data

import com.app.base.data.protocol.HttpResult
import com.app.common.api.network.Pager
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeApi {

    @GET("banner/json")
    suspend fun loadBanners(): HttpResult<List<Banner>>

    @GET("article/top/json")
    suspend fun loadTopArticles(): HttpResult<List<Article>>

    @GET("article/list/{page}/json")
    suspend fun loadArticles(@Path("page") page: Int, @Query("page_size") pageSize: Int): HttpResult<Pager<Article>>

}