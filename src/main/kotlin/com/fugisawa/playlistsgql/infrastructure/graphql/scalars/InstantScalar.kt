package com.fugisawa.playlistsgql.com.fugisawa.playlistsgql.infrastructure.graphql.scalars

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

val instantScalar =
    GraphQLScalarType
        .newScalar()
        .name("Instant")
        .description("Instant scalar")
        .coercing(InstantCoercing)
        .build()

object InstantCoercing : Coercing<Instant, String> {
    override fun parseValue(input: Any): Instant =
        try {
            Instant.parse(input.toString())
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Invalid Instant format: $input")
        }

    override fun parseLiteral(input: Any): Instant =
        try {
            Instant.parse((input as StringValue).value)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Invalid Instant format: ${(input as StringValue).value}")
        }

    override fun serialize(dataFetcherResult: Any): String =
        when (dataFetcherResult) {
            is Instant -> DateTimeFormatter.ISO_INSTANT.format(dataFetcherResult)
            else -> dataFetcherResult.toString()
        }
}
