package com.example.androidtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.anz.feature.users.data.UsersRepositoryImpl
import com.anz.feature.users.domain.GetUsersUseCase
import com.anz.feature.users.navigation.USERS_ROUTE
import com.anz.feature.users.navigation.usersGraph
import com.anz.feature.users.presentation.UsersViewModel
import com.anz.feature.users.ui.UsersFeatureRoot
import com.example.androidtask.ui.theme.AndroidTaskTheme
import com.example.core_network.RetrofitInstance
import com.example.core_network.UsersApi

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
  
        val usersApi = RetrofitInstance.api
        val usersRepository = UsersRepositoryImpl(usersApi)
        val getUsersUseCase = GetUsersUseCase(usersRepository)
        val usersViewModel = UsersViewModel(getUsersUseCase)
        setContent {
            UsersFeatureRoot(vm = usersViewModel)
        }
    }
}



