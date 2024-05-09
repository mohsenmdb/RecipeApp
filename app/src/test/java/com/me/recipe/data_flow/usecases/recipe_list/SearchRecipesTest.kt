package com.me.recipe.data_flow.usecases.recipe_list

import com.me.recipe.data_flow.cache.AppDatabaseFake
import com.me.recipe.data_flow.cache.RecipeDaoFake
import com.me.recipe.data_flow.data.MockWebServerResponses.recipeListResponse
import com.me.recipe.cache.features.recipe.mapper.RecipeEntityMapper
import com.me.recipe.data.features.recipe.mapper.RecipeMapper
import com.me.recipe.data.features.recipe_list.repository.RecipeListRepositoryImpl
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipe_list.repository.RecipeListRepository
import com.me.recipe.domain.features.recipe_list.usecases.SearchRecipesUsecase
import com.me.recipe.network.features.recipe.RecipeApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.HttpURLConnection

class SearchRecipesTest {

  private val appDatabase = AppDatabaseFake()
  private lateinit var mockWebServer: MockWebServer
  private lateinit var baseUrl: HttpUrl
  private val DUMMY_TOKEN = "gg335v5453453" // can be anything
  private val DUMMY_QUERY = "This doesn't matter" // can be anything

  // system in test
  private lateinit var searchRecipesUsecase: SearchRecipesUsecase

  // Dependencies
  private lateinit var recipeListRepository: RecipeListRepository
  private lateinit var recipeApi: RecipeApi
  private lateinit var recipeDao: RecipeDaoFake
  private val recipeMapper = RecipeMapper()
  private val entityMapper = RecipeEntityMapper()

  @BeforeEach
  fun setup() {
    mockWebServer = MockWebServer()
    mockWebServer.start()
    baseUrl = mockWebServer.url("/api/recipe/")
    recipeApi = Retrofit.Builder()
      .baseUrl(baseUrl)
      .addConverterFactory(
        MoshiConverterFactory.create(
          Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        )
      )
      .build()
      .create(RecipeApi::class.java)

    recipeDao = RecipeDaoFake(appDatabaseFake = appDatabase)
    recipeListRepository = RecipeListRepositoryImpl(
      recipeDao = recipeDao,
      recipeApi = recipeApi,
      entityMapper = entityMapper,
      recipeMapper = recipeMapper
    )
    // instantiate the system in test
    searchRecipesUsecase = SearchRecipesUsecase(recipeListRepository)
  }

  /**
   * 1. Are the recipes retrieved from the network?
   * 2. Are the recipes inserted into the cache?
   * 3. Are the recipes then emitted as a flow from the cache?
   */
  @Test
  fun getRecipesFromNetwork_emitRecipesFromCache(): Unit = runBlocking {

    // condition the response
    mockWebServer.enqueue(
      MockResponse()
        .setResponseCode(HttpURLConnection.HTTP_OK)
        .setBody(recipeListResponse)
    )

    // confirm the cache is empty to start
    assert(recipeDao.getAllRecipes(1, 30).isEmpty())

    val flowItems = searchRecipesUsecase.invoke(1, DUMMY_QUERY).toList()

    // confirm the cache is no longer empty
    assert(recipeDao.getAllRecipes(1, 30).isNotEmpty())

    // first emission should be `loading`
    assert(flowItems[0].loading)

    // Second emission should be the list of recipes
    val recipes = flowItems[1].data
    assert((recipes?.size ?: 0) > 0)

    // confirm they are actually Recipe objects
    assert(recipes?.get(index = 0) is Recipe)

    assert(!flowItems[1].loading) // loading should be false now
  }


  /**
   * Simulate a bad request
   */
  @Test
  fun getRecipesFromNetwork_emitHttpError(): Unit = runBlocking {

    // condition the response
    mockWebServer.enqueue(
      MockResponse()
        .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
        .setBody("{}")
    )

    val flowItems = searchRecipesUsecase.invoke(1, DUMMY_QUERY).toList()

    // first emission should be `loading`
    assert(flowItems[0].loading)

    // Second emission should be the exception
    val error = flowItems[1].error
    assert(error != null)

    assert(!flowItems[1].loading) // loading should be false now
  }

  @AfterEach
  fun tearDown() {
    mockWebServer.shutdown()
  }
}










