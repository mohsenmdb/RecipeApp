package com.me.recipe.data_flow.usecases.recipe_list

import com.me.recipe.data_flow.cache.AppDatabaseFake
import com.me.recipe.data_flow.cache.RecipeDaoFake
import com.me.recipe.data_flow.data.MockWebServerResponses
import com.google.gson.GsonBuilder
import com.me.recipe.cache.features.recipe.mapper.RecipeEntityMapper
import com.me.recipe.data.features.recipe.mapper.RecipeMapper
import com.me.recipe.data.features.recipe_list.repository.RecipeListRepositoryImpl
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipe_list.repository.RecipeListRepository
import com.me.recipe.domain.features.recipe_list.usecases.RestoreRecipesUsecase
import com.me.recipe.domain.features.recipe_list.usecases.SearchRecipesUsecase
import com.me.recipe.network.features.recipe.RecipeService
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class RestoreRecipesTest{

  private val appDatabase = AppDatabaseFake()
  private lateinit var mockWebServer: MockWebServer
  private lateinit var baseUrl: HttpUrl
  private val DUMMY_TOKEN = "gg335v5453453" // can be anything
  private val DUMMY_QUERY = "This doesn't matter" // can be anything

  // system in test
  private lateinit var restoreRecipesUsecase: RestoreRecipesUsecase

  // Dependencies
  private lateinit var recipeListRepository: RecipeListRepository
  private lateinit var searchRecipesUsecase: SearchRecipesUsecase
  private lateinit var recipeService: RecipeService
  private lateinit var recipeDao: RecipeDaoFake
  private val entityMapper = RecipeEntityMapper()
  private val recipeMapper = RecipeMapper()

  @BeforeEach
  fun setup(){
    mockWebServer = MockWebServer()
    mockWebServer.start()
    baseUrl = mockWebServer.url("/api/recipe/")
    recipeService = Retrofit.Builder()
      .baseUrl(baseUrl)
      .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
      .build()
      .create(RecipeService::class.java)
    recipeDao = RecipeDaoFake(appDatabaseFake = appDatabase)

    recipeListRepository = RecipeListRepositoryImpl(
      recipeDao = recipeDao,
      recipeService = recipeService,
      entityMapper = entityMapper,
      recipeMapper = recipeMapper
    )
    searchRecipesUsecase = SearchRecipesUsecase(recipeListRepository)

    // instantiate system in test
    restoreRecipesUsecase = RestoreRecipesUsecase(recipeListRepository)
  }

  /**
   * 1. Get some recipes from the network and insert into cache
   * 2. Restore and show recipes are retrieved from cache
   */
  @Test
  fun getRecipesFromNetwork_restoreFromCache(): Unit = runBlocking {

    // condition the response
    mockWebServer.enqueue(
      MockResponse()
        .setResponseCode(HttpURLConnection.HTTP_OK)
        .setBody(MockWebServerResponses.recipeListResponse)
    )

    // confirm the cache is empty to start
    assert(recipeDao.getAllRecipes(1, 30).isEmpty())

    // get recipes from network and insert into cache
    val searchResult = searchRecipesUsecase.invoke(1, DUMMY_QUERY).toList()

    // confirm the cache is no longer empty
    assert(recipeDao.getAllRecipes(1, 30).isNotEmpty())

    // run use case
    val flowItems = restoreRecipesUsecase.invoke(1, DUMMY_QUERY).toList()

    // first emission should be `loading`
    assert(flowItems[0].loading)

    // Second emission should be the list of recipes
    val recipes = flowItems[1].data
    assert(recipes?.size?: 0 > 0)

    // confirm they are actually Recipe objects
    assert(value = recipes?.get(index = 0) is Recipe)

    assert(!flowItems[1].loading) // loading should be false now
  }


  @AfterEach
  fun tearDown() {
    mockWebServer.shutdown()
  }
}
