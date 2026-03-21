package com.example.zomatoclone.presentation.ViewModels

import androidx.lifecycle.ViewModel
import com.example.zomatoclone.data.models.QuickScreenItems
import com.example.zomatoclone.domain.repo.QuickDeliveryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuickViewModel @Inject constructor(
    private val repo : QuickDeliveryRepo
) : ViewModel() {

    suspend fun getQuickDeliveryItem():List<QuickScreenItems>{
        return withContext(Dispatchers.IO ){

            repo.getQuickDeliveryItems()

        }
    }

}