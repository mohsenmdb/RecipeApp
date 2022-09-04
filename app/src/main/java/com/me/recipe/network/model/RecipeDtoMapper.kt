package com.me.recipe.network.model

import com.me.recipe.domain.model.Recipe
import com.me.recipe.domain.util.DomainMapper

class RecipeDtoMapper : DomainMapper<RecipeDto, Recipe> {
    override fun mapToDomainModel(model: RecipeDto): Recipe {
        return Recipe(
            id = model.pk,
            cookingInstructions = model.cookingInstructions,
            dateAdded = model.dateAdded,
            dateUpdated = model.dateUpdated,
            description = model.description,
            featuredImage = model.featuredImage,
            ingredients = model.ingredients ?: listOf(),
            longDateAdded = model.longDateAdded,
            longDateUpdated = model.longDateUpdated,
            publisher = model.publisher,
            rating = model.rating,
            sourceUrl = model.sourceUrl,
            title = model.title,
        )
    }

    override fun mapFromDomainModel(domainModel: Recipe): RecipeDto {
        return RecipeDto(
            pk = domainModel.id,
            cookingInstructions = domainModel.cookingInstructions,
            dateAdded = domainModel.dateAdded,
            dateUpdated = domainModel.dateUpdated,
            description = domainModel.description,
            featuredImage = domainModel.featuredImage,
            ingredients = domainModel.ingredients,
            longDateAdded = domainModel.longDateAdded,
            longDateUpdated = domainModel.longDateUpdated,
            publisher = domainModel.publisher,
            rating = domainModel.rating,
            sourceUrl = domainModel.sourceUrl,
            title = domainModel.title,
        )
    }

    fun toDomainList(initial : List<RecipeDto>): List<Recipe> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial : List<Recipe>): List<RecipeDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}