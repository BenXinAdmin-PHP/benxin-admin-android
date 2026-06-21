// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 内容公开接口 Retrofit 定义
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-21 23:12:00
// +----------------------------------------------------------------------
package com.benxin.admin.data.api

import com.benxin.admin.data.model.ApiEnvelope
import com.benxin.admin.data.model.Content
import com.benxin.admin.data.model.Paged
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * C 端公开接口（匿名可访问，N0 不带 token）。
 * 契约见 server: app/api/route/v1.php + app/api/controller/Content.php。
 */
interface ContentApi {

    /** 内容列表 GET /api/v1/contents（分页 + 可选 category_id/keyword 过滤） */
    @GET("api/v1/contents")
    suspend fun listContents(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 15,
        @Query("category_id") categoryId: Long? = null,
        @Query("keyword") keyword: String? = null,
    ): ApiEnvelope<Paged<Content>>

    /** 内容详情 GET /api/v1/contents/:id（访问会使 view_count +1） */
    @GET("api/v1/contents/{id}")
    suspend fun getContent(
        @Path("id") id: Long,
    ): ApiEnvelope<Content>
}
