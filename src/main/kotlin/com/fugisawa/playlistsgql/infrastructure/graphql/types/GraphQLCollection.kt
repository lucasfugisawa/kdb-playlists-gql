package com.fugisawa.playlistsgql.infrastructure.graphql.types

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore

data class PageInfo(
    val offset: Int,
    val limit: Int,
    val hasNextPage: Boolean,
)

@GraphQLIgnore
interface GraphQLCollection<T> {
    val items: List<T>
    val totalCount: Int
    val pageInfo: PageInfo
}

fun <T, R> pagedCollection(
    allItems: List<T>,
    filteredItems: List<T>,
    offset: Int? = null,
    limit: Int? = null,
    transform: (T) -> R,
): Triple<List<R>, Int, PageInfo> {
    val effectiveOffset = offset ?: 0
    val effectiveLimit = limit ?: filteredItems.size

    val pagedItems =
        filteredItems
            .drop(effectiveOffset)
            .take(effectiveLimit)
            .map(transform)

    val hasNextPage = filteredItems.size > effectiveOffset + effectiveLimit

    val pageInfo =
        PageInfo(
            offset = effectiveOffset,
            limit = effectiveLimit,
            hasNextPage = hasNextPage,
        )

    return Triple(pagedItems, filteredItems.size, pageInfo)
}
