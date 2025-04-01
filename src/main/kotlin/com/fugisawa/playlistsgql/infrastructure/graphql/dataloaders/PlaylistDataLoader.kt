package com.fugisawa.playlistsgql.infrastructure.graphql.dataloaders

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.fugisawa.playlistsgql.domain.models.Playlist
import com.fugisawa.playlistsgql.domain.services.PlaylistService
import graphql.GraphQLContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import org.dataloader.DataLoaderOptions
import java.util.UUID

class PlaylistDataLoader(private val playlistService: PlaylistService) : KotlinDataLoader<UUID, Playlist?> {
    override val dataLoaderName = "PlaylistDataLoader"

    override fun getDataLoader(graphQLContext: GraphQLContext): DataLoader<UUID, Playlist?> {
        return DataLoaderFactory.newDataLoader(
            { ids, batchLoaderEnvironment ->
                // Get the CoroutineScope from the GraphQL context
                val coroutineScope = batchLoaderEnvironment.getContext<GraphQLContext>()?.get<CoroutineScope>(CoroutineScope::class)
                    ?: CoroutineScope(EmptyCoroutineContext)

                // Use the future extension function to convert the coroutine to a CompletableFuture
                coroutineScope.future {
                    val playlists = playlistService.getAll()
                    val playlistMap = playlists.associateBy { it.id }
                    ids.map { playlistMap[it] }
                }
            },
            DataLoaderOptions.newOptions()
                .setBatchLoaderContextProvider { graphQLContext }
        )
    }
}
