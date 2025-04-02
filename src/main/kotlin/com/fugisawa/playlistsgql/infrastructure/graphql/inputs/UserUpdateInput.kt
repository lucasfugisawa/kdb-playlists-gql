package com.fugisawa.playlistsgql.infrastructure.graphql.inputs

import com.expediagroup.graphql.generator.execution.OptionalInput

data class UserUpdateInput(
    val username: OptionalInput<String> = OptionalInput.Undefined,
)
