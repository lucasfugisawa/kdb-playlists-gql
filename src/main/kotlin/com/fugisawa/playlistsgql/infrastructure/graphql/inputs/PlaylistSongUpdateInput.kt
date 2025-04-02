package com.fugisawa.playlistsgql.infrastructure.graphql.inputs

import com.expediagroup.graphql.generator.execution.OptionalInput

data class PlaylistSongUpdateInput(
    val position: OptionalInput<Int> = OptionalInput.Undefined,
)