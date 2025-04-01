package com.fugisawa.playlistsgql.infrastructure.graphql.dataloaders

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.fugisawa.playlistsgql.domain.models.Vote
import com.fugisawa.playlistsgql.domain.services.VoteService
import graphql.GraphQLContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import org.dataloader.DataLoaderOptions
import java.util.UUID

class VoteDataLoader(private val voteService: VoteService) : KotlinDataLoader<UUID, Vote?> {
    override val dataLoaderName = "VoteDataLoader"

    override fun getDataLoader(graphQLContext: GraphQLContext): DataLoader<UUID, Vote?> {
        return DataLoaderFactory.newDataLoader(
            { ids, batchLoaderEnvironment ->
                // Get the CoroutineScope from the GraphQL context
                val coroutineScope = batchLoaderEnvironment.getContext<GraphQLContext>()?.get<CoroutineScope>(CoroutineScope::class)
                    ?: CoroutineScope(EmptyCoroutineContext)

                // Use the future extension function to convert the coroutine to a CompletableFuture
                coroutineScope.future {
                    val votes = voteService.getAll()
                    val voteMap = votes.associateBy { it.id }
                    ids.map { voteMap[it] }
                }
            },
            DataLoaderOptions.newOptions()
                .setBatchLoaderContextProvider { graphQLContext }
        )
    }
}
