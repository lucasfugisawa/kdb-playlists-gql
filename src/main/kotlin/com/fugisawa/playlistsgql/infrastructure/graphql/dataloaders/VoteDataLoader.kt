package com.fugisawa.playlistsgql.infrastructure.graphql.dataloaders

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.fugisawa.playlistsgql.domain.entities.Vote
import com.fugisawa.playlistsgql.services.VoteService
import graphql.GraphQLContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import org.dataloader.DataLoaderOptions
import java.util.UUID
import kotlin.coroutines.EmptyCoroutineContext

class VoteDataLoader(
    private val voteService: VoteService,
) : KotlinDataLoader<UUID, Vote?> {
    override val dataLoaderName = "VoteDataLoader"

    override fun getDataLoader(graphQLContext: GraphQLContext): DataLoader<UUID, Vote?> =
        DataLoaderFactory.newDataLoader(
            { ids, batchLoaderEnvironment ->
                val coroutineScope =
                    batchLoaderEnvironment.getContext<GraphQLContext>()?.get<CoroutineScope>(CoroutineScope::class)
                        ?: CoroutineScope(EmptyCoroutineContext)

                coroutineScope.future {
                    val votes = voteService.getByIds(ids)
                    val voteMap = votes.associateBy { it.id }
                    ids.map { voteMap[it] }
                }
            },
            DataLoaderOptions
                .newOptions()
                .setBatchLoaderContextProvider { graphQLContext },
        )
}
