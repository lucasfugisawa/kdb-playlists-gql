package com.fugisawa.playlistsgql.com.fugisawa.playlistsgql.infrastructure.graphql.subscriptions

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Subscription
import graphql.GraphqlErrorException
import graphql.execution.DataFetcherResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asPublisher
import org.reactivestreams.Publisher
import kotlin.random.Random

data class IntWrapper(val value: Int)

class ExampleSubscriptionService : Subscription {

    @GraphQLDescription("Returns a single value")
    fun singleValue(): Flow<IntWrapper> = flowOf(IntWrapper(42))

    @GraphQLDescription("Returns stream of values")
    fun multipleValues(): Flow<Int> = flowOf(1, 2, 3)

    @GraphQLDescription("Returns a random number every second")
    fun counter(limit: Int? = null): Flow<Int> = flow {
        var count = 0
        while (true) {
            count++
            if (limit != null) {
                if (count > limit) break
            }
            emit(Random.Default.nextInt())
            delay(1000)
        }
    }

    @GraphQLDescription("Returns a random number every second, errors if even")
    fun counterWithError(): Flow<Int> = flow {
        while (true) {
            val value = Random.Default.nextInt()
            if (value % 2 == 0) {
                throw Exception("Value is even $value")
            } else emit(value)
            delay(1000)
        }
    }

    @GraphQLDescription("Returns one value then an error")
    fun singleValueThenError(): Flow<Int> = flowOf(1, 2)
        .map { if (it == 2) throw Exception("Second value") else it }

    @GraphQLDescription("Returns stream of errors")
    fun flowOfErrors(): Publisher<DataFetcherResult<String?>> {
        val dfr: DataFetcherResult<String?> = DataFetcherResult.newResult<String?>()
            .data(null)
            .error(GraphqlErrorException.newErrorException().cause(Exception("error thrown")).build())
            .build()

        return flowOf(dfr, dfr).asPublisher()
    }
}