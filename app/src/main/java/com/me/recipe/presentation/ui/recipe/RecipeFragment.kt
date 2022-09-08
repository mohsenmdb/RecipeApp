package com.me.recipe.presentation.ui.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.me.recipe.presentation.ui.recipe_list.RecipeListViewModel
import com.me.recipe.ui.theme.RecipeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeFragment : Fragment() {

//    private val viewModel: RecipeListViewModel by viewModels()
    private val viewModel: RecipeListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        println("hey -------- > ${viewModel.name}")
        return ComposeView(requireContext()).apply {
            setContent {
                PageContent()
            }
        }
    }

    @Composable
    fun PageContent() {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "Recipe Page 1")
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        RecipeTheme {
            PageContent()
        }
    }
}
