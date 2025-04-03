package com.fugisawa.playlistsgql.infrastructure.graphql.dataloaders

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.fugisawa.playlistsgql.domain.entities.Song
import com.fugisawa.playlistsgql.domain.services.SongService
import graphql.GraphQLContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import org.dataloader.DataLoaderOptions
import java.util.UUID
import kotlin.coroutines.EmptyCoroutineContext

class SongDataLoader(
    private val songService: SongService,
) : KotlinDataLoader<UUID, Song?> {
    override val dataLoaderName = "SongDataLoader"

    override fun getDataLoader(graphQLContext: GraphQLContext): DataLoader<UUID, Song?> =
        DataLoaderFactory.newDataLoader(
            { ids, batchLoaderEnvironment ->
                val coroutineScope =
                    batchLoaderEnvironment.getContext<GraphQLContext>()?.get<CoroutineScope>(CoroutineScope::class)
                        ?: CoroutineScope(EmptyCoroutineContext)

                coroutineScope.future {
                    val songs = songService.getAll()
                    val songMap = songs.associateBy { it.id }
                    ids.map { songMap[it] }
                }
            },
            DataLoaderOptions
                .newOptions()
                .setBatchLoaderContextProvider { graphQLContext },
        )
}
