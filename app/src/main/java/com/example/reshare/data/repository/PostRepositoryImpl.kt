package com.example.reshare.data.repository

import coil.network.HttpException
import com.example.reshare.data.local.post.PostDao
import com.example.reshare.data.mapper.toDomain
import com.example.reshare.data.mapper.toEntity
import com.example.reshare.data.remote.AppApi
import com.example.reshare.domain.model.Like
import com.example.reshare.domain.model.PagedResult
import com.example.reshare.domain.model.Post
import com.example.reshare.domain.repository.PostRepository
import com.example.reshare.presentation.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.IOException

class PostRepositoryImpl (
    private val api: AppApi,
    private val dao: PostDao
) : PostRepository {

    override suspend fun getPosts(
        forceFetchFromRemote: Boolean,
        page: Int
    ): Flow<Resource<PagedResult<Post>>> = flow {
        /***
         * Flow để xử lý luồng bất đồng bộ, khác với suspend chỉ trả về 1 giá trị duy nhất
         * Flow có thể phát ra (emit) nhiều giá trị liên tiếp (chuỗi hành động)
         * Mỗi lần gọi emit, dữ liệu sẽ được gửi đến nơi đang thu thập Flow
         */
        emit(Resource.Loading(true))

        // Lấy dữ liệu từ roomBd
        val localPosts = withContext(Dispatchers.IO) {
            dao.getAllPosts()
        }

        val shouldUseCache = localPosts.isNotEmpty() && !forceFetchFromRemote

        if (shouldUseCache) {
            emit(Resource.Success(
                PagedResult(
                    data = localPosts.map { it.toDomain() }
                )
            ))
            emit(Resource.Loading(false))
            // Kết thúc luồng dữ liệu
            return@flow
        }

        val postListFromApi = try {
            api.getPosts(page)
        } catch (e: IOException) {
            emit(Resource.Error("Network error: ${e.message}"))
            emit(Resource.Loading(false))
            return@flow
        } catch (e: HttpException) {
            emit(Resource.Error("Server error: ${e.message}"))
            emit(Resource.Loading(false))
            return@flow
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.message}"))
            emit(Resource.Loading(false))
            return@flow
        }

        val postEntities = postListFromApi.posts.let {
            it.map { postDto ->
                postDto.toEntity()
            }
        }

        dao.upsertPosts(postEntities)
        emit(
            Resource.Success(
                PagedResult(
                    data = postEntities.map { it.toDomain() },
                    currentPage = postListFromApi.currentPage,
                    totalPages = postListFromApi.totalPages
                )
            ))
        emit(Resource.Loading(false))
    }

    override suspend fun toggleLike(postId: String): Resource<Like> {
        return try {
            val likeDto = api.toggleLike(postId)
            Resource.Success(Like(likes = likeDto.likes, liked = likeDto.liked))
        } catch (e: IOException) {
            Resource.Error("Network error: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Server error: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Unexpected error: ${e.message}")
        }
    }
}



