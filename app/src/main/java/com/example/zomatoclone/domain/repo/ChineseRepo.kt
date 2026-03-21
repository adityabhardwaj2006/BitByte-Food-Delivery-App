package com.example.zomatoclone.domain.repo

import com.example.zomatoclone.data.models.ChineseFood

interface ChineseRepo {
     suspend fun getChineseFood(): List<ChineseFood>
}