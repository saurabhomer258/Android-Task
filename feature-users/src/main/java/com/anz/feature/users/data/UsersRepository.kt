package com.anz.feature.users.data

import com.anz.core.model.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun getUsers(): Flow<List<User>>
}