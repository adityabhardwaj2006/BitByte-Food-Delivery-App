package com.example.zomatoclone.presentation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zomatoclone.R
import com.example.zomatoclone.data.models.FoodCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.collections.emptyList

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(): ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _foodList = MutableStateFlow(allFood)
    val foodList = _searchText
        .debounce (500L)
        .combine(_foodList) { text, items ->

            if (text.isBlank()) {
                emptyList()
            } else {
                items.filter {
                    it.searchInQuery(text)
                }
            }

        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )


    fun onTextSearch(text: String) : Unit{
            _searchText.value = text
    }

}

data class Food(
    val name : String,
    val imageRes : Int
){

    val combinations = listOf(
        name,
        name.first().toString(),
        name.last().toString()
    )
    fun searchInQuery(query: String) : Boolean{
        return  combinations.any{
            it.contains(query, ignoreCase = true)
        }
    }

}
private val allFood = listOf(
    Food( name = "ALL", imageRes =  R.drawable. allfood),
    Food(name = "Burger", imageRes = R.drawable.burger),
    Food(name="Pizza", imageRes = R.drawable.pizza_image),
    Food(name = "Sweets",imageRes =  R.drawable. sweets),
    Food( name = "Biryani",imageRes =  R.drawable.vegbiryani),
    Food ( name = "Ice Cream", imageRes = R.drawable.ice_cream),
    Food(  name = "Rolls",imageRes =  R.drawable.chinese),
    Food( name = "Pasta",imageRes =  R.drawable. pasta),
    Food(name = "Chinese", imageRes = R.drawable.chinese),
)