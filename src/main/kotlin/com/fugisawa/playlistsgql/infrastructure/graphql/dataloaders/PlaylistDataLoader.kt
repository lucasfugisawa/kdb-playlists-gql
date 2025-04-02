package com.fugisawa.playlistsgql.infrastructure.graphql.dataloaders

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.fugisawa.playlistsgql.domain.models.Playlist
import com.fugisawa.playlistsgql.domain.services.PlaylistService
import graphql.GraphQLContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import org.dataloader.DataLoaderOptions
import java.util.UUID
import kotlin.coroutines.EmptyCoroutineContext

class PlaylistDataLoader(
    private val playlistService: PlaylistService,
) : KotlinDataLoader<UUID, Playlist?> {
    override val dataLoaderName = "PlaylistDataLoader"

    override fun getDataLoader(graphQLContext: GraphQLContext): DataLoader<UUID, Playlist?> =
        DataLoaderFactory.newDataLoader(
            { ids, batchLoaderEnvironment ->
                val coroutineScope =
                    batchLoaderEnvironment.getContext<GraphQLContext>()?.get<CoroutineScope>(CoroutineScope::class)
                        ?: CoroutineScope(EmptyCoroutineContext)

                coroutineScope.future {
                    val playlists = playlistService.getAll()
                    val playlistMap = playlists.associateBy { it.id }
                    ids.map { playlistMap[it] }
                }
            },
            DataLoaderOptions
                .newOptions()
                .setBatchLoaderContextProvider { graphQLContext },
        )
}
