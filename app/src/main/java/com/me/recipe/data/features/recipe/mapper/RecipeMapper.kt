package com.me.recipe.data.features.recipe.mapper

import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.util.DomainMapper
import com.me.recipe.network.features.recipe.model.RecipeNetwork
import com.me.recipe.util.DateUtils
import kotlinx.collections.immutable.toPersistentList

class RecipeMapper : DomainMapper<RecipeNetwork, Recipe> {

    override fun mapToDomainModel(model: RecipeNetwork): Recipe {
        return Recipe(
            id = model.pk ?: -1,
            title = model.title.orEmpty(),
            featuredImage = model.featuredImage.orEmpty(),
            rating = model.rating,
            publisher = model.publisher.orEmpty(),
            sourceUrl = model.sourceUrl.orEmpty(),
            ingredients = model.ingredients.toPersistentList(),
            dateAdded = DateUtils.longToDate(model.longDateAdded ?: 0),
            dateUpdated = DateUtils.longToDate(model.longDateUpdated ?: 0),
        )
    }

    override fun mapFromDomainModel(domainModel: Recipe): RecipeNetwork {
        return RecipeNetwork(
            pk = domainModel.id,
            title = domainModel.title,
            featuredImage = domainModel.featuredImage,
            rating = domainModel.rating,
            publisher = domainModel.publisher,
            sourceUrl = domainModel.sourceUrl,
            ingredients = domainModel.ingredients,
            longDateAdded = DateUtils.dateToLong(domainModel.dateAdded),
            longDateUpdated = DateUtils.dateToLong(domainModel.dateUpdated),
        )
    }

    fun toDomainList(initial : List<RecipeNetwork>): List<Recipe> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial : List<Recipe>): List<RecipeNetwork> {
        return initial.map { mapFromDomainModel(it) }
    }
}