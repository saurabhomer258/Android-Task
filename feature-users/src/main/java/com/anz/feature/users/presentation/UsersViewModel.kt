package com.anz.feature.users.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anz.core.model.User
import com.anz.feature.users.domain.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface UsersUiState {
    object Loading : UsersUiState
    data class Success(val users: List<User>) : UsersUiState
    data class Error(val message: String) : UsersUiState
}


class UsersViewModel constructor(
    private val getUsers: GetUsersUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<UsersUiState>(UsersUiState.Loading)
    val state: StateFlow<UsersUiState> = _state.asStateFlow()
    
    
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()
    
    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser: StateFlow<User?> = _selectedUser.asStateFlow()
    
    
    init { refresh() }
    
    
    fun refresh() {
        _state.value = UsersUiState.Loading
        viewModelScope.launch {
            runCatching { getUsers() }
                .onFailure { _state.value = UsersUiState.Error(it.message ?: "Unknown error") }
                .onSuccess { flow ->
                    flow.collect { list ->
                        _state.value = UsersUiState.Success(list)
                    }
                }
        }
    }
    
    
    fun loadUsers() {
        viewModelScope.launch {
            getUsers().collect { _users.value = it }
        }
    }
    
    fun selectUser(user: User) {
        _selectedUser.value = user
    }
    
    fun backToList() {
        _selectedUser.value = null
    }
}