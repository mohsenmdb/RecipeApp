package com.me.recipe.network.model

import com.me.recipe.domain.model.Recipe
import com.me.recipe.domain.util.DomainMapper
import com.me.recipe.util.DateUtils

class RecipeDtoMapper : DomainMapper<RecipeDto, Recipe> {

    override fun mapToDomainModel(model: RecipeDto): Recipe {
        return Recipe(
            id = model.pk ?: 0,
            title = model.title.orEmpty(),
            featuredImage = model.featuredImage.orEmpty(),
            rating = model.rating,
            publisher = model.publisher.orEmpty(),
            sourceUrl = model.sourceUrl.orEmpty(),
            ingredients = model.ingredients,
            dateAdded = DateUtils.longToDate(model.longDateAdded ?: 0),
            dateUpdated = DateUtils.longToDate(model.longDateUpdated ?: 0),
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
            longDateAdded = DateUtils.dateToLong(domainModel.dateAdded),
            longDateUpdated = DateUtils.dateToLong(domainModel.dateUpdated),
        )
    }

    fun toDomainList(initial : List<RecipeDto>): List<Recipe> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial : List<Recipe>): List<RecipeDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}