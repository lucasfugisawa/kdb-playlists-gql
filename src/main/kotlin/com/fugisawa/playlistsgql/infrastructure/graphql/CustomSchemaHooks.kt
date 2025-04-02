package com.fugisawa.playlistsgql.infrastructure.graphql

import com.expediagroup.graphql.generator.hooks.FlowSubscriptionSchemaGeneratorHooks
import com.fugisawa.playlistsgql.infrastructure.graphql.scalars.instantScalar
import com.fugisawa.playlistsgql.infrastructure.graphql.scalars.uuidScalar
import graphql.schema.GraphQLType
import java.time.Instant
import java.util.UUID
import kotlin.reflect.KType

class CustomSchemaHooks : FlowSubscriptionSchemaGeneratorHooks() {
    override fun willGenerateGraphQLType(type: KType): GraphQLType? =
        when (type.classifier) {
            UUID::class -> uuidScalar
            Instant::class -> instantScalar
            else -> null
        }
}
