package com.me.recipe.dataflow.usecases.recipelist

import com.me.recipe.cache.features.recipe.mapper.RecipeEntityMapper
import com.me.recipe.data.features.recipe.mapper.RecipeMapper
import com.me.recipe.data.features.recipelist.repository.RecipeListRepositoryImpl
import com.me.recipe.dataflow.cache.AppDatabaseFake
import com.me.recipe.dataflow.cache.RecipeDaoFake
import com.me.recipe.dataflow.data.MockWebServerResponses.RECIPE_LIST_RESPONSE
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.repository.RecipeListRepository
import com.me.recipe.domain.features.recipelist.usecases.SearchRecipesUsecase
import com.me.recipe.network.features.recipe.RecipeApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.net.HttpURLConnection
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SearchRecipesTest {

    private val appDatabase = AppDatabaseFake()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val dummyToken = "gg335v5453453" // can be anything
    private val dummyQuery = "This doesn't matter" // can be anything

    // system in test
    private lateinit var searchRecipesUsecase: com.me.recipe.domain.features.recipelist.usecases.SearchRecipesUsecase

    // Dependencies
    private lateinit var recipeListRepository: com.me.recipe.domain.features.recipelist.repository.RecipeListRepository
    private lateinit var recipeApi: RecipeApi
    private lateinit var recipeDao: RecipeDaoFake
    private val recipeMapper = RecipeMapper()
    private val entityMapper = RecipeEntityMapper()

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("/api/recipe/")
        recipeApi = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build(),
                ),
            )
            .build()
            .create(RecipeApi::class.java)

        recipeDao = RecipeDaoFake(appDatabaseFake = appDatabase)
        recipeListRepository = RecipeListRepositoryImpl(
            recipeDao = recipeDao,
            recipeApi = recipeApi,
            entityMapper = entityMapper,
            recipeMapper = recipeMapper,
        )
        // instantiate the system in test
        searchRecipesUsecase =
            com.me.recipe.domain.features.recipelist.usecases.SearchRecipesUsecase(
                recipeListRepository
            )
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
                .setBody(RECIPE_LIST_RESPONSE),
        )

        // confirm the cache is empty to start
        assert(recipeDao.getAllRecipes(1, 30).isEmpty())

        val flowItems = searchRecipesUsecase.invoke(1, dummyQuery).toList()

        // confirm the cache is no longer empty
        assert(recipeDao.getAllRecipes(1, 30).isNotEmpty())

        // first emission should be `loading`
        assert(flowItems[0].loading)

        // Second emission should be the list of recipes
        val recipes = flowItems[1].data
        assert((recipes?.size ?: 0) > 0)

        // confirm they are actually Recipe objects
        assert(recipes?.get(index = 0) is com.me.recipe.domain.features.recipe.model.Recipe)

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
                .setBody("{}"),
        )

        val flowItems = searchRecipesUsecase.invoke(1, dummyQuery).toList()

        // first emission should be `loading`
        assert(flowItems[0].loading)

        // Second emission should be the exception
        val error = flowItems[1].error
        assert(error != null)

        assert(!flowItems[1].loading) // loading should be false now
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
