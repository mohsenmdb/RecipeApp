package com.me.recipe.dataflow.usecases.recipe

import com.me.recipe.cache.features.recipe.mapper.RecipeEntityMapper
import com.me.recipe.data.features.recipe.mapper.RecipeMapper
import com.me.recipe.data.features.recipe.repository.RecipeRepositoryImpl
import com.me.recipe.data.features.recipelist.repository.RecipeListRepositoryImpl
import com.me.recipe.dataflow.cache.AppDatabaseFake
import com.me.recipe.dataflow.cache.RecipeDaoFake
import com.me.recipe.dataflow.data.MockWebServerResponses
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipe.repository.RecipeRepository
import com.me.recipe.domain.features.recipe.usecases.GetRecipeUsecase
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

class GetRecipeTest {

    private val appDatabase = AppDatabaseFake()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val dummyToken = "gg335v5453453" // can be anything
    private val dummyQuery = "This doesn't matter" // can be anything

    // system in test
    private lateinit var getRecipeUsecase: GetRecipeUsecase
    private val recipeId = 1551
    private lateinit var recipeListRepository: RecipeListRepository
    private lateinit var recipeRepository: RecipeRepository

    // Dependencies
    private lateinit var searchRecipesUsecase: SearchRecipesUsecase
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

        recipeRepository = RecipeRepositoryImpl(
            recipeDao = recipeDao,
            recipeApi = recipeApi,
            entityMapper = entityMapper,
            recipeMapper = recipeMapper,
        )
        searchRecipesUsecase = SearchRecipesUsecase(recipeListRepository)

        // instantiate the system in test
        getRecipeUsecase = GetRecipeUsecase(recipeRepository)
    }

    /**
     * 1. Get some recipes from the network and insert into cache
     * 2. Try to retrieve recipes by their specific recipe id
     */
    @Test
    fun getRecipesFromNetwork_getRecipeById(): Unit = runBlocking {
        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponses.RECIPE_LIST_RESPONSE),
        )

        // confirm the cache is empty to start
        assert(recipeDao.getAllRecipes(1, 30).isEmpty())

        // get recipes from network and insert into cache
        val searchResult = searchRecipesUsecase.invoke(1, dummyQuery).toList()

        // confirm the cache is no longer empty
        assert(recipeDao.getAllRecipes(1, 30).isNotEmpty())

        // run use case
        val recipeAsFlow = getRecipeUsecase.invoke(recipeId, true).toList()

        // first emission should be `loading`
        assert(recipeAsFlow[0].loading)

        // second emission should be the recipe
        val recipe = recipeAsFlow[1].data
        assert(recipe?.id == recipeId)

        // confirm it is actually a Recipe object
        assert(recipe is Recipe)

        // 'loading' should be false now
        assert(!recipeAsFlow[1].loading)
    }

    /**
     * 1. Try to get a recipe that does not exist in the cache
     * Result should be:
     * 1. Recipe is retrieved from network and inserted into cache
     * 2. Recipe is returned as flow from cache
     */
    @Test
    fun attemptGetNullRecipeFromCache_getRecipeById(): Unit = runBlocking {
        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponses.RECIPE_WITH_ID_1551),
        )

        // confirm the cache is empty to start
        assert(recipeDao.getAllRecipes(1, 30).isEmpty())

        // run use case
        val recipeAsFlow = getRecipeUsecase.invoke(recipeId, true).toList()

        // first emission should be `loading`
        assert(recipeAsFlow[0].loading)

        // second emission should be the recipe
        val recipe = recipeAsFlow[1].data
        assert(recipe?.id == recipeId)

        // confirm the recipe is in the cache now
        assert(recipeDao.getRecipeById(recipeId)?.id == recipeId)

        // confirm it is actually a Recipe object
        assert(recipe is Recipe)

        // 'loading' should be false now
        assert(!recipeAsFlow[1].loading)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
