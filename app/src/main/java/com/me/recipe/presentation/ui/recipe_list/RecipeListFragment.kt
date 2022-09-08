package com.me.recipe.presentation.ui.recipe_list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.me.recipe.R
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    private val viewModel: RecipeListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        println(viewModel.apiToken)

        return ComposeView(requireContext()).apply {
            setContent {
                val recipes = viewModel.recipes.value
                for (recipe in recipes){
                    Log.d(TAG, recipe.title ?: "")
                }

                PageContent()
            }
        }
    }

    @Composable
    fun PageContent() {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Recipe List ",
                style = TextStyle(fontSize = 24.sp)
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Button(onClick = {
                findNavController().navigate(R.id.action_recipeListFragment_to_recipePageFragment)
            }) {
                Text(text = "Go To Page")
            }
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
