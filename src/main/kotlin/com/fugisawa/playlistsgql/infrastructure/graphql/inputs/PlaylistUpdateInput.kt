package com.fugisawa.playlistsgql.infrastructure.graphql.inputs

import com.expediagroup.graphql.generator.execution.OptionalInput

data class PlaylistUpdateInput(
    val title: OptionalInput<String> = OptionalInput.Undefined,
    val description: OptionalInput<String?> = OptionalInput.Undefined,
    val tags: OptionalInput<List<String>?> = OptionalInput.Undefined,
)
