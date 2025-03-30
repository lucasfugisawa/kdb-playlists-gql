package com.fugisawa.playlistsgql.com.fugisawa.playlistsgql.infrastructure.graphql.scalars

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import java.util.UUID

val uuidScalar = GraphQLScalarType.newScalar()
    .name("UUID")
    .description("UUID scalar")
    .coercing(UUIDCoercing)
    .build()

object UUIDCoercing : Coercing<UUID, String> {
    override fun parseValue(input: Any): UUID = UUID.fromString(input.toString())
    override fun parseLiteral(input: Any): UUID = UUID.fromString((input as StringValue).value)
    override fun serialize(dataFetcherResult: Any): String = dataFetcherResult.toString()
}
