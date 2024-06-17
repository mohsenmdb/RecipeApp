package com.me.recipe.data.features.recipe.mapper

import com.me.recipe.core.utils.DateUtils
import com.me.recipe.network.features.recipe.model.RecipeNetwork
import kotlinx.collections.immutable.toPersistentList

class RecipeMapper :
    com.me.recipe.domain.util.DomainMapper<RecipeNetwork, com.me.recipe.domain.features.recipe.model.Recipe> {

    override fun mapToDomainModel(model: RecipeNetwork): com.me.recipe.domain.features.recipe.model.Recipe {
        return com.me.recipe.domain.features.recipe.model.Recipe(
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

    override fun mapFromDomainModel(domainModel: com.me.recipe.domain.features.recipe.model.Recipe): RecipeNetwork {
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

    fun toDomainList(initial: List<RecipeNetwork>): List<com.me.recipe.domain.features.recipe.model.Recipe> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<com.me.recipe.domain.features.recipe.model.Recipe>): List<RecipeNetwork> {
        return initial.map { mapFromDomainModel(it) }
    }
}
