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
            val viewModel = hiltViewModel<ImageViewModel>()
            var query by rememberSaveable { mutableStateOf("") }
            ImageSearchAppTheme {
                Scaffold(modifier = Modifier
                    .safeContentPadding()
                    .fillMaxSize(),
                    topBar = {
                        TextField(modifier = Modifier.fillMaxWidth(),
                            value = query,
                            onValueChange = {
                                query = it
                                viewModel.updateQuery(query)
                            })
                    }) { innerPadding ->
                    MainContent(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(), viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier, viewModel: ImageViewModel) {

    val paging = viewModel.images.collectAsLazyPagingItems()


    if (paging.loadState.refresh is LoadState.Error) {
        Button(modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(), onClick = {
            paging.retry()
        }) {
            Text("Retry")
        }
    }

    if (paging.loadState.refresh is LoadState.Loading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    if (paging.loadState.refresh is LoadState.NotLoading) {
        if (paging.itemCount == 0) {
            Box(
                modifier = Modifier.fillMaxSize(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Nothing found")
            }
        }
    }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2)
    ) {
        item {
            if (paging.loadState.prepend is LoadState.Loading) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }

        item {
            if (paging.loadState.prepend is LoadState.Error) {
                Button(modifier = Modifier
                    .fillMaxWidth(), onClick = {
                    paging.retry()
                }) {
                    Text("Retry")
                }
            }
        }

        if (paging.loadState.refresh is LoadState.NotLoading) {
            if (paging.itemCount != 0) {
                items(
                    count = paging.itemCount,
                    key = paging.itemKey { it.uuid },
                    contentType = paging.itemContentType { "contentType" }) { index ->
                    paging[index]?.let { item ->
                        AsyncImage(
                            model = item.imageUrl,
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
        }

        item {
            if (paging.loadState.append is LoadState.Loading) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
        item {
            if (paging.loadState.append is LoadState.Error) {
                Button(modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(), onClick = {
                    paging.retry()
                }) {
                    Text("Retry")
                }
            }
        }
    }


}

