package com.me.recipe.data.features.recipe_list.repository

import com.me.recipe.cache.features.recipe.RecipeDao
import com.me.recipe.cache.features.recipe.mapper.RecipeEntityMapper
import com.me.recipe.data.core.utils.DataState
import com.me.recipe.data.features.recipe.mapper.RecipeMapper
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.network.features.recipe.RecipeService
import com.me.recipe.domain.features.recipe_list.repository.RecipeListRepository
import com.me.recipe.util.RECIPE_PAGINATION_PAGE_SIZE
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecipeListRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val recipeService: RecipeService,
    private val entityMapper: RecipeEntityMapper,
    private val recipeMapper: RecipeMapper,
) : RecipeListRepository {
    override suspend fun search(
        page: Int,
        query: String
    ): Flow<DataState<List<Recipe>>> =
        flow {
            try {
                emit(DataState.loading())
                val recipes = getRecipesFromNetwork(page = page, query = query)
                recipeDao.insertRecipes(entityMapper.toEntityList(recipes))
            } catch (e: Exception) {
                emit(DataState.error(e.message ?: "Unknown Error"))
            }

            // query the cache
            val cacheResult = if (query.isBlank()) {
                recipeDao.getAllRecipes(
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            } else {
                recipeDao.searchRecipes(
                    query = query,
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            }

            val list = entityMapper.fromEntityList(cacheResult)
            emit(DataState.success(list))
        }

    override suspend fun restore(page: Int, query: String): Flow<DataState<List<Recipe>>> = flow {
        try {
            emit(DataState.loading())

            // just to show pagination, api is fast
            delay(1000)

            // query the cache
            val cacheResult = if (query.isBlank()) {
                recipeDao.restoreAllRecipes(
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            } else {
                recipeDao.restoreRecipes(
                    query = query,
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            }

            // emit List<Recipe> from cache
            val list = entityMapper.fromEntityList(cacheResult)
            emit(DataState.success(list))

        } catch (e: Exception) {
            emit(DataState.error<List<Recipe>>(e.message ?: "Unknown Error"))
        }
    }


    // WARNING: This will throw exception if there is no network connection
    private suspend fun getRecipesFromNetwork(
        page: Int,
        query: String
    ): List<Recipe> {
        return recipeMapper.toDomainList(
            recipeService.search(
                page = page,
                query = query
            ).results
        )
    }
}