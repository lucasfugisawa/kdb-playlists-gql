package com.fugisawa.playlistsgql.infrastructure.graphql.dataloaders

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.fugisawa.playlistsgql.domain.models.Song
import com.fugisawa.playlistsgql.domain.services.SongService
import graphql.GraphQLContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import org.dataloader.DataLoaderOptions
import java.util.UUID

class SongDataLoader(private val songService: SongService) : KotlinDataLoader<UUID, Song?> {
    override val dataLoaderName = "SongDataLoader"

    override fun getDataLoader(graphQLContext: GraphQLContext): DataLoader<UUID, Song?> {
        return DataLoaderFactory.newDataLoader(
            { ids, batchLoaderEnvironment ->
                // Get the CoroutineScope from the GraphQL context
                val coroutineScope = batchLoaderEnvironment.getContext<GraphQLContext>()?.get<CoroutineScope>(CoroutineScope::class)
                    ?: CoroutineScope(EmptyCoroutineContext)

                // Use the future extension function to convert the coroutine to a CompletableFuture
                coroutineScope.future {
                    val songs = songService.getAll()
                    val songMap = songs.associateBy { it.id }
                    ids.map { songMap[it] }
                }
            },
            DataLoaderOptions.newOptions()
                .setBatchLoaderContextProvider { graphQLContext }
        )
    }
}
