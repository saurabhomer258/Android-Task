package com.anz.core.model
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val company: String? = null,
    val username: String? = null,
    val email: String? = null,
    val address: String? = null,
    val zip: String? = null,
    val state: String? = null,
    val country: String? = null,
    val phone: String? = null,
    val photo: String? = null
)