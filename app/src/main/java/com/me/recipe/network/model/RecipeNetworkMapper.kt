package com.me.recipe.network.model

import com.me.recipe.domain.model.Recipe
import com.me.recipe.domain.util.EntityMapper

class RecipeNetworkMapper : EntityMapper<RecipeNetworkEntity, Recipe> {
    override fun mapFromEntity(entity: RecipeNetworkEntity): Recipe {
        return Recipe(
            id = entity.pk,
            cookingInstructions = entity.cookingInstructions,
            dateAdded = entity.dateAdded,
            dateUpdated = entity.dateUpdated,
            description = entity.description,
            featuredImage = entity.featuredImage,
            ingredients = entity.ingredients ?: listOf(),
            longDateAdded = entity.longDateAdded,
            longDateUpdated = entity.longDateUpdated,
            publisher = entity.publisher,
            rating = entity.rating,
            sourceUrl = entity.sourceUrl,
            title = entity.title,
        )
    }

    override fun mapToEntity(domainModel: Recipe): RecipeNetworkEntity {
        return RecipeNetworkEntity(
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

    fun fromEntityList(initial : List<RecipeNetworkEntity>): List<Recipe> {
        return initial.map { mapFromEntity(it) }
    }

    fun toEntityList(initial : List<Recipe>): List<RecipeNetworkEntity> {
        return initial.map { mapToEntity(it) }
    }
}