package com.anz.feature.users.data

import com.anz.core.model.User
import com.example.core_network.UsersApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UsersRepositoryImpl(
    private val api: UsersApi
) : UsersRepository {
    override fun getUsers(): Flow<List<User>> = flow {
        emit(api.getUsers())
    }
}