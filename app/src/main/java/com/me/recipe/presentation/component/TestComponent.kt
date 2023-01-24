package com.me.recipe.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable


@Composable
fun MyBottomNav() {
    BottomNavigation {
        BottomNavigationItem(selected = false, onClick = {}, icon = {
            Icon(
                Icons.Default.Search,
                contentDescription = ""
            )
        })
        BottomNavigationItem(selected = false, onClick = {}, icon = {
            Icon(
                Icons.Default.Add,
                contentDescription = ""
            )
        })
        BottomNavigationItem(selected = true, onClick = {}, icon = {
            Icon(
                Icons.Default.Star,
                contentDescription = ""
            )
        })
        BottomNavigationItem(selected = false, onClick = {}, icon = {
            Icon(
                Icons.Default.Send,
                contentDescription = ""
            )
        })
        BottomNavigationItem(selected = false, onClick = {}, icon = {
            Icon(
                Icons.Default.List,
                contentDescription = ""
            )
        })
    }
}
@Composable
fun MyDrawer() {
    Column {
        Text(text = "nav drawer for test")
        Text(text = "nav drawer for test")
        Text(text = "nav drawer for test")
        Text(text = "nav drawer for test")
    }
}