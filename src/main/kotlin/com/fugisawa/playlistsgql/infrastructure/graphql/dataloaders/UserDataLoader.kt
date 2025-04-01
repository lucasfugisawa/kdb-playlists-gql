package com.fugisawa.playlistsgql.infrastructure.graphql.dataloaders

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.fugisawa.playlistsgql.domain.models.User
import com.fugisawa.playlistsgql.domain.services.UserService
import graphql.GraphQLContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import org.dataloader.DataLoaderOptions
import java.util.UUID

class UserDataLoader(private val userService: UserService) : KotlinDataLoader<UUID, User?> {
    override val dataLoaderName = "UserDataLoader"

    override fun getDataLoader(graphQLContext: GraphQLContext): DataLoader<UUID, User?> {
        return DataLoaderFactory.newDataLoader(
            { ids, batchLoaderEnvironment ->
                // Get the CoroutineScope from the GraphQL context
                val coroutineScope = batchLoaderEnvironment.getContext<GraphQLContext>()?.get<CoroutineScope>(CoroutineScope::class)
                    ?: CoroutineScope(EmptyCoroutineContext)

                // Use the future extension function to convert the coroutine to a CompletableFuture
                coroutineScope.future {
                    val users = userService.getAll()
                    val userMap = users.associateBy { it.id }
                    ids.map { userMap[it] }
                }
            },
            DataLoaderOptions.newOptions()
                .setBatchLoaderContextProvider { graphQLContext }
        )
    }
}
