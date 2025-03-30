package com.fugisawa.playlistsgql.infrastructure.graphql

import com.expediagroup.graphql.generator.hooks.FlowSubscriptionSchemaGeneratorHooks
import com.fugisawa.playlistsgql.com.fugisawa.playlistsgql.infrastructure.graphql.scalars.uuidScalar
import graphql.schema.GraphQLType
import java.util.*
import kotlin.reflect.KType

class CustomSchemaHooks() : FlowSubscriptionSchemaGeneratorHooks() {
    override fun willGenerateGraphQLType(type: KType): GraphQLType? {
        return if (type.classifier == UUID::class) uuidScalar else null
    }
}
