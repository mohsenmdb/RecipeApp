package com.me.recipe.data.features.recipelist.repository

import com.me.recipe.cache.features.recipe.RecipeDao
import com.me.recipe.cache.features.recipe.mapper.RecipeEntityMapper
import com.me.recipe.core.data.DataState
import com.me.recipe.data.features.recipe.mapper.RecipeMapper
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.repository.RecipeListRepository
import com.me.recipe.network.features.recipe.RecipeApi
import com.me.recipe.util.RECIPE_PAGINATION_PAGE_SIZE
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecipeListRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val recipeApi: RecipeApi,
    private val entityMapper: RecipeEntityMapper,
    private val recipeMapper: RecipeMapper,
) : com.me.recipe.domain.features.recipelist.repository.RecipeListRepository {
    override suspend fun search(
        page: Int,
        query: String,
    ): Flow<DataState<ImmutableList<com.me.recipe.domain.features.recipe.model.Recipe>>> =
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
                    page = page,
                )
            } else {
                recipeDao.searchRecipes(
                    query = query,
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page,
                )
            }

            val list = entityMapper.fromEntityList(cacheResult).toPersistentList()
            emit(DataState.success(list))
        }

    override suspend fun restore(page: Int, query: String): Flow<DataState<ImmutableList<com.me.recipe.domain.features.recipe.model.Recipe>>> = flow {
        try {
            emit(DataState.loading())

            // just to show pagination, api is fast
            delay(1000)

            // query the cache
            val cacheResult = if (query.isBlank()) {
                recipeDao.restoreAllRecipes(
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page,
                )
            } else {
                recipeDao.restoreRecipes(
                    query = query,
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page,
                )
            }

            // emit List<Recipe> from cache
            val list = entityMapper.fromEntityList(cacheResult).toPersistentList()
            emit(DataState.success(list))
        } catch (e: Exception) {
            emit(DataState.error(e.message ?: "Unknown Error"))
        }
    }

    override suspend fun getTopRecipe(): com.me.recipe.domain.features.recipe.model.Recipe {
        return getRecipesFromNetwork(1, "").firstOrNull() ?: com.me.recipe.domain.features.recipe.model.Recipe.EMPTY
    }

    // WARNING: This will throw exception if there is no network connection
    private suspend fun getRecipesFromNetwork(
        page: Int,
        query: String,
    ): List<com.me.recipe.domain.features.recipe.model.Recipe> {
        return recipeMapper.toDomainList(
            recipeApi.search(
                page = page,
                query = query,
            ).results,
        )
    }
}
