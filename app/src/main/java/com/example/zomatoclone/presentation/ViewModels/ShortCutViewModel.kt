package com.example.zomatoclone.presentation.ViewModels

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ShortCutViewModel @Inject constructor() : ViewModel() {

    private var _tabName = MutableStateFlow<String?>(null)
    var tabName = _tabName.asStateFlow()

    fun updateTabName(name : String){
        _tabName.value = name
    }


}