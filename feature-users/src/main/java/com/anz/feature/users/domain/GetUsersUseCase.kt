package com.anz.feature.users.domain

import com.anz.core.model.User
import com.anz.feature.users.data.UsersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    operator fun invoke(): Flow<List<User>> = repository.getUsers()
}