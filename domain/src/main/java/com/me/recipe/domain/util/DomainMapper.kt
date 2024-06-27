package com.me.recipe.domain.util

interface DomainMapper<T, DomainModel> {
    fun mapToDomainModel(model: T, uid: String? = null): DomainModel
    fun mapFromDomainModel(domainModel: DomainModel): T
}
