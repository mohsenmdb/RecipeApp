package com.me.recipe.cache.features.recipe.mapper

import com.me.recipe.cache.features.recipe.model.RecipeEntity
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.util.DomainMapper
import com.me.recipe.util.DateUtils
import kotlinx.collections.immutable.toPersistentList

class RecipeEntityMapper :
    com.me.recipe.domain.util.DomainMapper<RecipeEntity, com.me.recipe.domain.features.recipe.model.Recipe> {

    override fun mapToDomainModel(model: RecipeEntity): com.me.recipe.domain.features.recipe.model.Recipe {
        return com.me.recipe.domain.features.recipe.model.Recipe(
            id = model.id,
            title = model.title,
            featuredImage = model.featuredImage,
            rating = model.rating,
            publisher = model.publisher,
            sourceUrl = model.sourceUrl,
            ingredients = convertIngredientsToList(model.ingredients).toPersistentList(),
            dateAdded = DateUtils.longToDate(model.dateAdded),
            dateUpdated = DateUtils.longToDate(model.dateUpdated),
        )
    }

    override fun mapFromDomainModel(domainModel: com.me.recipe.domain.features.recipe.model.Recipe): RecipeEntity {
        return RecipeEntity(
            id = domainModel.id,
            title = domainModel.title,
            featuredImage = domainModel.featuredImage,
            rating = domainModel.rating,
            publisher = domainModel.publisher,
            sourceUrl = domainModel.sourceUrl,
            ingredients = convertIngredientListToString(domainModel.ingredients),
            dateAdded = DateUtils.dateToLong(domainModel.dateAdded),
            dateUpdated = DateUtils.dateToLong(domainModel.dateUpdated),
            dateCached = DateUtils.dateToLong(DateUtils.createTimestamp()),
        )
    }

    /**
     * "Carrot, potato, Chicken, ..."
     */
    private fun convertIngredientListToString(ingredients: List<String>): String {
        val ingredientsString = StringBuilder()
        for (ingredient in ingredients) {
            ingredientsString.append("$ingredient,")
        }
        return ingredientsString.toString()
    }

    private fun convertIngredientsToList(ingredientsString: String?): List<String> {
        val list: ArrayList<String> = ArrayList()
        ingredientsString?.let {
            for (ingredient in it.split(",")) {
                list.add(ingredient)
            }
        }
        return list
    }

    fun fromEntityList(initial: List<RecipeEntity>): List<com.me.recipe.domain.features.recipe.model.Recipe> {
        return initial.map { mapToDomainModel(it) }
    }

    fun toEntityList(initial: List<com.me.recipe.domain.features.recipe.model.Recipe>): List<RecipeEntity> {
        return initial.map { mapFromDomainModel(it) }
    }
}
