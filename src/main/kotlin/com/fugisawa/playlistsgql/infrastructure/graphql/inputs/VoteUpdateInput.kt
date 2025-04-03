package com.fugisawa.playlistsgql.infrastructure.graphql.inputs

import com.expediagroup.graphql.generator.execution.OptionalInput
import com.fugisawa.playlistsgql.domain.entities.VoteType

data class VoteUpdateInput(
    val type: OptionalInput<VoteType> = OptionalInput.Undefined,
)
