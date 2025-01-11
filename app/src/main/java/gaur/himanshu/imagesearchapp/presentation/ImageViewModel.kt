package gaur.himanshu.imagesearchapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gaur.himanshu.imagesearchapp.domain.useCase.GetImagesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class ImageViewModel @Inject constructor(
    private val getImagesUseCase: GetImagesUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")

    val images = _query
        .filter { it.isNotBlank() }
        .debounce(1000)
        .flatMapLatest { query ->
            getImagesUseCase.invoke(query).flow
        }.cachedIn(viewModelScope)

    fun updateQuery(q:String) = _query.update { q }

}