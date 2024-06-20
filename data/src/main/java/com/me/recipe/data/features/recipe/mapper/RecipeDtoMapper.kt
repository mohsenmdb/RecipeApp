package com.me.recipe.data.features.recipe.mapper

import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.util.DomainMapper
import com.me.recipe.network.features.recipe.model.RecipeDto
import kotlinx.collections.immutable.toPersistentList

class RecipeDtoMapper :
    DomainMapper<RecipeDto, Recipe> {

    override fun mapToDomainModel(model: RecipeDto): Recipe {
        return Recipe(
            id = model.pk ?: -1,
            title = model.title.orEmpty(),
            featuredImage = model.featuredImage.orEmpty(),
            rating = model.rating,
            publisher = model.publisher.orEmpty(),
            sourceUrl = model.sourceUrl.orEmpty(),
            ingredients = model.ingredients.toPersistentList(),
            date = model.dateUpdated.orEmpty(),
            dateTimestamp = model.dateUpdatedTimeStamp ?: 0L,
        )
    }

    override fun mapFromDomainModel(domainModel: Recipe): RecipeDto {
        return RecipeDto(
            pk = domainModel.id,
            title = domainModel.title,
            featuredImage = domainModel.featuredImage,
            rating = domainModel.rating,
            publisher = domainModel.publisher,
            sourceUrl = domainModel.sourceUrl,
            ingredients = domainModel.ingredients,
            dateUpdated = domainModel.date,
            dateUpdatedTimeStamp = domainModel.dateTimestamp,
        )
    }

    fun toDomainList(initial: List<RecipeDto>): List<Recipe> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Recipe>): List<RecipeDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}
