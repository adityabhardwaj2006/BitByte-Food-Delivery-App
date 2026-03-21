package com.example.zomatoclone.domain.repo

import com.example.zomatoclone.data.models.Pasta

interface PastaRepo {
    suspend fun getPastaList () : List<Pasta>
}