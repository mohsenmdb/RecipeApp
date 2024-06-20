package com.me.recipe.cache.recipe.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "publisher")
    var publisher: String,

    @ColumnInfo(name = "featured_image")
    var featuredImage: String,

    @ColumnInfo(name = "rating")
    var rating: Int,

    @ColumnInfo(name = "source_url")
    var sourceUrl: String,

    /**
     * Value from API
     * Comma separated list of ingredients
     * EX: "carrots, cabbage, chicken,"
     */
    @ColumnInfo(name = "ingredients")
    var ingredients: String = "",

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "date_timestamp")
    var dateTimestamp: Long,

    /**
     * The date this recipe was "refreshed" in the cache.
     */
    @ColumnInfo(name = "date_cached")
    var dateCached: Long,
)
