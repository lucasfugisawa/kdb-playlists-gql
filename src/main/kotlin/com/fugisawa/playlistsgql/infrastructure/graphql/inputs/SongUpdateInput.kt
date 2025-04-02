package com.fugisawa.playlistsgql.infrastructure.graphql.inputs

import com.expediagroup.graphql.generator.execution.OptionalInput
import com.fugisawa.playlistsgql.data.models.enums.Genre

data class SongUpdateInput(
    val title: OptionalInput<String> = OptionalInput.Undefined,
    val artist: OptionalInput<String> = OptionalInput.Undefined,
    val duration: OptionalInput<Int> = OptionalInput.Undefined,
    val genre: OptionalInput<Genre> = OptionalInput.Undefined,
)
