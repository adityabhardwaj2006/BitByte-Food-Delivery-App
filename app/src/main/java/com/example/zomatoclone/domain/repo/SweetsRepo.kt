package com.example.zomatoclone.domain.repo

import com.example.zomatoclone.data.models.Sweets

interface SweetsRepo {
    suspend fun getSweetsList() : List<Sweets>
}