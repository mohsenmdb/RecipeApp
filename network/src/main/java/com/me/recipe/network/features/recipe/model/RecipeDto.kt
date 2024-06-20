package com.me.recipe.network.features.recipe.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeDto(
    @Json(name = "pk") var pk: Int?,
    @Json(name = "title") var title: String?,
    @Json(name = "publisher") var publisher: String?,
    @Json(name = "featured_image") var featuredImage: String?,
    @Json(name = "rating") var rating: Int = 0,
    @Json(name = "source_url") var sourceUrl: String?,
    @Json(name = "ingredients") var ingredients: List<String> = emptyList(),
    @Json(name = "long_date_updated") var dateUpdatedTimeStamp: Long?,
    @Json(name = "date_updated") var dateUpdated: String?,
)
