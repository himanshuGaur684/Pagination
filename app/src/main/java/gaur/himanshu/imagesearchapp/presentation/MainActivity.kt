package gaur.himanshu.imagesearchapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import gaur.himanshu.imagesearchapp.presentation.ui.theme.ImageSearchAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var query by rememberSaveable { mutableStateOf("") }
            val viewModel = hiltViewModel<MainViewModel>()
            ImageSearchAppTheme {
                Scaffold(
                    topBar = {
                        TextField(value = query, onValueChange = {
                            query = it
                            viewModel.updateQuery(query)
                        }, modifier = Modifier.fillMaxWidth())
                    },
                    modifier = Modifier
                        .safeContentPadding()
                        .fillMaxSize(),
                ) { innerPadding ->
                    MainContent(modifier = Modifier.padding(innerPadding), viewModel)
                }
            }
        }
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier, viewModel: MainViewModel) {

    val images = viewModel.images.collectAsLazyPagingItems()

    if (images.loadState.refresh is LoadState.NotLoading) {

        if (images.itemCount == 0) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nothing found")
            }
        }

    }

    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = modifier) {

        if (images.loadState.prepend is LoadState.Loading) {
            item {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
        if (images.loadState.prepend is LoadState.Error) {
            item {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Button(modifier = Modifier.fillMaxWidth(), onClick = {
                        images.retry()
                    }) { Text("Retry") }
                }
            }
        }


        if (images.loadState.refresh is LoadState.NotLoading) {
            if (images.itemCount != 0) {

                items(
                    count = images.itemCount,
                    key = images.itemKey { it.uuid },
                    contentType = images.itemContentType { "contenttype" },
                ) { index ->
                    AsyncImage(
                        model = images.get(index)?.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }

            }
        }



        if (images.loadState.append is LoadState.Loading) {
            item {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
        if (images.loadState.append is LoadState.Error) {
            item {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Button(modifier = Modifier.fillMaxWidth(), onClick = {
                        images.retry()
                    }) { Text("Retry") }
                }
            }
        }

    }


}
