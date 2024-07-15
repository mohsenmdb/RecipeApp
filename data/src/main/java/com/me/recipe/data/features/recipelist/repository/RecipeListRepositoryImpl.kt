package com.me.recipe.data.features.recipelist.repository

import com.me.recipe.cache.recipe.RecipeDao
import com.me.recipe.data.features.recipe.mapper.RecipeDtoMapper
import com.me.recipe.data.features.recipe.mapper.RecipeEntityMapper
import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.repository.RecipeListRepository
import com.me.recipe.network.features.recipe.RecipeApi
import com.me.recipe.shared.data.DataState
import com.me.recipe.shared.utils.FoodCategory
import com.me.recipe.shared.utils.RECIPE_CATEGORY_PAGE_SIZE
import com.me.recipe.shared.utils.RECIPE_PAGINATION_PAGE_SIZE
import com.me.recipe.shared.utils.RECIPE_SLIDER_PAGE_SIZE
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeListRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val recipeApi: RecipeApi,
    private val entityMapper: RecipeEntityMapper,
    private val dtoMapper: RecipeDtoMapper,
) : RecipeListRepository {
    override suspend fun search(
        page: Int,
        query: String,
        size: Int,
    ): Flow<DataState<ImmutableList<Recipe>>> = flow {
        try {
            emit(DataState.loading())
            val recipes = getRecipesFromNetwork(page = page, query = query, size = size)
            recipeDao.insertRecipes(entityMapper.toEntityList(recipes))
        } catch (e: Exception) {
            emit(DataState.error(e))
        }

        // query the cache
        val cacheResult = if (query.isBlank()) {
            recipeDao.getAllRecipes(
                pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                page = page,
            )
        } else {
            recipeDao.searchRecipes(
                pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                query = query,
                page = page,
            )
        }

        val list = entityMapper.toDomainList(cacheResult).toPersistentList()
        emit(DataState.success(list))
    }

    override suspend fun categoriesRecipes(categories: ImmutableList<FoodCategory>): Flow<DataState<ImmutableList<CategoryRecipe>>> =
        flow {
            emit(DataState.loading())
            try {
                withContext(Dispatchers.IO) {
                    launch {
                        val runningTasks = categories.map { category ->
                            async { // this will allow us to run multiple tasks in parallel
                                val apiResponse = getRecipesFromNetwork(
                                    page = 1,
                                    query = category.name,
                                    size = RECIPE_CATEGORY_PAGE_SIZE,
                                )
                                category to apiResponse // associate category and response for later
                            }
                        }
                        val responses = runningTasks.awaitAll()
                        responses.forEach {
                            recipeDao.insertRecipes(entityMapper.toEntityList(it.second))
                        }
                    }
                }
            } catch (e: Exception) {
                emit(DataState.error(e))
            }

            val list = buildList {
                categories.forEach { category ->
                    val cacheResult = recipeDao.searchRecipes(
                        pageSize = RECIPE_CATEGORY_PAGE_SIZE,
                        query = category.name,
                        page = 1,
                    )
                    val recipes = entityMapper.toDomainList(cacheResult).toPersistentList()
                    add(CategoryRecipe(category, recipes))
                }
            }.toPersistentList()

            emit(DataState.success(list))
        }

    override suspend fun slider(): Flow<DataState<ImmutableList<Recipe>>> = flow {
        try {
            emit(DataState.loading())
            val recipes =
                getRecipesFromNetwork(page = 1, size = RECIPE_SLIDER_PAGE_SIZE, query = "")
            recipeDao.insertRecipes(entityMapper.toEntityList(recipes))
        } catch (e: Exception) {
            emit(DataState.error(e))
        }

        // query the cache
        val cacheResult = recipeDao.getAllRecipes(
            pageSize = RECIPE_SLIDER_PAGE_SIZE,
            page = 1,
        )

        val list = entityMapper.toDomainList(cacheResult).toPersistentList()
        emit(DataState.success(list))
    }

    override suspend fun restore(page: Int, query: String): Flow<DataState<ImmutableList<Recipe>>> =
        flow {
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
                val list = entityMapper.toDomainList(cacheResult).toPersistentList()
                emit(DataState.success(list))
            } catch (e: Exception) {
                emit(DataState.error(e))
            }
        }

    override suspend fun getTopRecipe(): Recipe {
        return try {
            getRecipesFromNetwork(1, "", 1).firstOrNull() ?: Recipe.EMPTY
        } catch (e: Exception) {
            Recipe.EMPTY
        }
    }

    private suspend fun getRecipesFromNetwork(page: Int, query: String, size: Int): List<Recipe> {
        val recipes = recipeApi.search(page = page, query = query, size = size).results
        return dtoMapper.toDomainList(recipes)
    }
}
