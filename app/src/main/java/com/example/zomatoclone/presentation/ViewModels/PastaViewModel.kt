package com.example.zomatoclone.presentation.ViewModels

import androidx.lifecycle.ViewModel
import com.example.zomatoclone.data.models.Pasta
import com.example.zomatoclone.data.repoImpl.RepoImpl
import com.example.zomatoclone.domain.repo.PastaRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PastaViewModel @Inject constructor(
    private val pastaRepo : PastaRepo
) : ViewModel(){

    suspend fun getPastaList() : List<Pasta>{
        return withContext(Dispatchers.IO){
            pastaRepo.getPastaList()
        }
    }

}