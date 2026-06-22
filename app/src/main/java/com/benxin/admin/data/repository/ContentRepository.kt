// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 内容仓库（协程 + Result 包裹）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-21 23:12:00
// +----------------------------------------------------------------------
package com.benxin.admin.data.repository

import com.benxin.admin.data.api.ApiClient
import com.benxin.admin.data.api.ContentApi
import com.benxin.admin.data.model.Category
import com.benxin.admin.data.model.Content
import com.benxin.admin.data.model.Paged

/** 业务码非 0 时抛出，携带 code 与后端 msg。 */
class ApiException(val code: Int, message: String) : Exception(message)

/**
 * 内容仓库：把 ApiEnvelope 拆封为 Result，网络/业务异常统一收敛。
 * 默认依赖 [ApiClient.contentApi]（手动注入，便于测试替换）。
 */
class ContentRepository(
    private val api: ContentApi = ApiClient.contentApi,
) {

    suspend fun listContents(
        page: Int = 1,
        pageSize: Int = 15,
        categoryId: Long? = null,
        keyword: String? = null,
    ): Result<Paged<Content>> = runCatching {
        val resp = api.listContents(page, pageSize, categoryId, keyword)
        if (resp.code != 0 || resp.data == null) {
            throw ApiException(resp.code, resp.msg.ifBlank { "请求失败（code=${resp.code}）" })
        }
        resp.data
    }

    suspend fun getContent(id: Long): Result<Content> = runCatching {
        val resp = api.getContent(id)
        if (resp.code != 0 || resp.data == null) {
            throw ApiException(resp.code, resp.msg.ifBlank { "请求失败（code=${resp.code}）" })
        }
        resp.data
    }

    suspend fun getCategories(): Result<List<Category>> = runCatching {
        val resp = api.getCategories()
        if (resp.code != 0 || resp.data == null) {
            throw ApiException(resp.code, resp.msg.ifBlank { "请求失败（code=${resp.code}）" })
        }
        resp.data
    }
}
