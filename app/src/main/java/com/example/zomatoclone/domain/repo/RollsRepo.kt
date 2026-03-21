package com.example.zomatoclone.domain.repo

import com.example.zomatoclone.data.models.Rolls

interface RollsRepo {
    suspend fun getRollsList() : List<Rolls>
}