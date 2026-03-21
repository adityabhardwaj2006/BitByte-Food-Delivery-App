package com.example.zomatoclone.domain.repo

import com.example.zomatoclone.data.models.Biryani

interface BiryaniRepo {

    suspend fun getBiryaniList() : List<Biryani>

}
