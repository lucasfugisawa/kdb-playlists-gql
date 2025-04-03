package com.fugisawa.playlistsgql.infrastructure.graphql.dataloaders

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.fugisawa.playlistsgql.domain.entities.PlaylistSong
import com.fugisawa.playlistsgql.services.PlaylistSongService
import graphql.GraphQLContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import org.dataloader.DataLoaderOptions
import java.util.UUID
import kotlin.coroutines.EmptyCoroutineContext

class PlaylistSongDataLoader(
    private val playlistSongService: PlaylistSongService,
) : KotlinDataLoader<UUID, PlaylistSong?> {
    override val dataLoaderName = "PlaylistSongDataLoader"

    override fun getDataLoader(graphQLContext: GraphQLContext): DataLoader<UUID, PlaylistSong?> =
        DataLoaderFactory.newDataLoader(
            { ids, batchLoaderEnvironment ->
                val coroutineScope =
                    batchLoaderEnvironment.getContext<GraphQLContext>()?.get<CoroutineScope>(CoroutineScope::class)
                        ?: CoroutineScope(EmptyCoroutineContext)

                coroutineScope.future {
                    val playlistSongs = playlistSongService.getByIds(ids)
                    val playlistSongMap = playlistSongs.associateBy { it.id }
                    ids.map { playlistSongMap[it] }
                }
            },
            DataLoaderOptions
                .newOptions()
                .setBatchLoaderContextProvider { graphQLContext },
        )
}
