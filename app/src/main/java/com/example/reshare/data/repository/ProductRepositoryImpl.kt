package com.example.reshare.data.repository

import android.util.Log
import com.example.reshare.data.local.product.ProductDao
import com.example.reshare.data.mapper.toDomain
import com.example.reshare.data.mapper.toEntity
import com.example.reshare.data.remote.AppApi
import com.example.reshare.domain.model.CategorizedProducts
import com.example.reshare.domain.model.Product
import com.example.reshare.domain.repository.ProductRepository
import com.example.reshare.presentation.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ProductRepositoryImpl (
    private val api: AppApi,
    private val dao: ProductDao
) : ProductRepository {
    override suspend fun getCategorizedProducts(
        forceFetchFromRemote: Boolean
    ): Flow<Resource<CategorizedProducts>> = flow {
        emit(Resource.Loading(true))

        // Lấy dữ liệu từ roomBd
        val localCategorizedProducts = withContext(Dispatchers.IO) {
            val freeFood = dao.getFreeFood()
            val nonFood = dao.getNonFood()
            val reducedFood = dao.getReducedFood()
            val want = dao.getWant()

            CategorizedProducts(
                freeFood = freeFood.map { it.toDomain() },
                nonFood = nonFood.map { it.toDomain() },
                reducedFood = reducedFood.map { it.toDomain() },
                want = want.map { it.toDomain() }
            )
        }

        val shouldUseCache = (
                localCategorizedProducts.freeFood.isNotEmpty() ||
                        localCategorizedProducts.nonFood.isNotEmpty() ||
                        localCategorizedProducts.reducedFood.isNotEmpty() ||
                        localCategorizedProducts.want.isNotEmpty()
                ) && !forceFetchFromRemote

        if (shouldUseCache) {
            emit(Resource.Success(localCategorizedProducts))
            emit(Resource.Loading(false))
            return@flow
        }

        val categorizedProductsFromApi = try {
            api.getCategorizedProducts()
        }catch (e: IOException) {
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

        val productEntities = categorizedProductsFromApi.let {
            buildList {
                addAll(it.freeFood)
                addAll(it.nonFood)
                addAll(it.reducedFood)
                addAll(it.want)
            }.map { product -> product.toEntity() }
        }

        dao.upsertProducts(productEntities)
        emit(Resource.Success(categorizedProductsFromApi.toDomain()))
        emit(Resource.Loading(false))
    }

    override suspend fun getNearbyProducts(
        forceFetchFromRemote: Boolean,
        query: String?
    ): Flow<Resource<List<Product>>> =  flow {
        emit(Resource.Loading(true))

        val productsFromApi = try {
            api.getAllNearbyProducts(query)
        }catch (e: IOException) {
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

        emit(Resource.Success(productsFromApi.products.map { it.toDomain() }))
        emit(Resource.Loading(false))
    }
}