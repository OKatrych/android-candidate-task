package dev.olek.kordon.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TodoListResponse(
    @SerialName("code")
    val code: Int? = null,
    @SerialName("meta")
    val meta: MetaResponse? = null,
    @SerialName("data")
    val data: List<TodoResponse>? = null
)