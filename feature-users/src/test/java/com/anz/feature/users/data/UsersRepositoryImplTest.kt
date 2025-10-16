package com.anz.feature.users.data

import com.anz.core.model.User
import com.example.core_network.UsersApi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first

class UsersRepositoryImplTest {
    
    private val api: UsersApi = mockk()
    
    @Test
    fun `getUsers emits users from api`() = runTest {
        // Arrange
        val sample = listOf(
            User(id = 1, name = "Alice", email = "alice@example.com", company = "ACME"),
            User(id = 2, name = "Bob", email = "bob@example.com")
        )
        coEvery { api.getUsers() } returns sample
        
        val repo = UsersRepositoryImpl(api)
        
        // Act
        val emitted = repo.getUsers().first()
        
        // Assert
        assertThat(emitted).containsExactlyElementsIn(sample)
    }
}