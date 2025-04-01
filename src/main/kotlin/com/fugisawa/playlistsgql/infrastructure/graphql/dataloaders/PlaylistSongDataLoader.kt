package com.fugisawa.playlistsgql.infrastructure.graphql.dataloaders

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.fugisawa.playlistsgql.domain.models.PlaylistSong
import com.fugisawa.playlistsgql.domain.services.PlaylistSongService
import graphql.GraphQLContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import org.dataloader.DataLoaderOptions
import java.util.UUID

class PlaylistSongDataLoader(private val playlistSongService: PlaylistSongService) : KotlinDataLoader<UUID, PlaylistSong?> {
    override val dataLoaderName = "PlaylistSongDataLoader"

    override fun getDataLoader(graphQLContext: GraphQLContext): DataLoader<UUID, PlaylistSong?> {
        return DataLoaderFactory.newDataLoader(
            { ids, batchLoaderEnvironment ->
                // Get the CoroutineScope from the GraphQL context
                val coroutineScope = batchLoaderEnvironment.getContext<GraphQLContext>()?.get<CoroutineScope>(CoroutineScope::class)
                    ?: CoroutineScope(EmptyCoroutineContext)

                // Use the future extension function to convert the coroutine to a CompletableFuture
                coroutineScope.future {
                    val playlistSongs = playlistSongService.getAll()
                    val playlistSongMap = playlistSongs.associateBy { it.id }
                    ids.map { playlistSongMap[it] }
                }
            },
            DataLoaderOptions.newOptions()
                .setBatchLoaderContextProvider { graphQLContext }
        )
    }
}
