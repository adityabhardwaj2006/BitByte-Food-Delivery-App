package com.example.zomatoclone.domain.repo

import com.example.zomatoclone.data.models.QuickScreenItems

interface QuickDeliveryRepo {
    suspend fun getQuickDeliveryItems() : List<QuickScreenItems>
}