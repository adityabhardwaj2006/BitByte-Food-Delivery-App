package com.example.zomatoclone.data.dataModule

import android.content.Context
import androidx.room.Room
import com.example.zomatoclone.data.OrderDatabase
import com.example.zomatoclone.domain.Api.AllCategoryApi
import com.example.zomatoclone.domain.Api.BiryaniApi
import com.example.zomatoclone.domain.Api.BurgersApi
import com.example.zomatoclone.domain.Api.ChineseApi
import com.example.zomatoclone.domain.Api.IceCreamApi
import com.example.zomatoclone.domain.Api.PastaApi
import com.example.zomatoclone.domain.Api.QuickDeliveryApi
import com.example.zomatoclone.domain.Api.RestaurantApi
import com.example.zomatoclone.domain.Api.RollsApi
import com.example.zomatoclone.domain.Api.SweetsApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//Provide and manage shared objects (dependencies) across your app
//Instead of manually creating things like FirebaseAuth, Retrofit, or your database everywhere,
//you define them once here and let Hilt inject them wherever needed
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }
    @Singleton
    @Provides
    fun provideFireStore() : FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun getRetrofitPizza(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.jsonbin.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Singleton
    @Provides
    fun pizzaApi(retrofit: Retrofit) : RestaurantApi{
        return retrofit.create(RestaurantApi::class.java)
    }

    @Singleton
    @Provides
    fun chineseApi(retrofit: Retrofit): ChineseApi{
        return retrofit.create(ChineseApi::class.java)
    }

    @Singleton
    @Provides
    fun allApi(retrofit: Retrofit): AllCategoryApi{
        return retrofit.create(AllCategoryApi::class.java)
    }

    @Singleton
    @Provides
    fun birApi(retrofit: Retrofit) : BiryaniApi{
        return retrofit.create(BiryaniApi::class.java)
    }

    @Singleton
    @Provides
    fun burgApi(retrofit: Retrofit) : BurgersApi{
        return retrofit.create(BurgersApi::class.java)
    }

    @Singleton
    @Provides
    fun rollsApi(retrofit: Retrofit) : RollsApi{
        return retrofit.create(RollsApi::class.java)
    }

    @Singleton
    @Provides
    fun sweetsApi(retrofit: Retrofit) : SweetsApi{
        return retrofit.create(SweetsApi::class.java)
    }

    @Singleton
    @Provides
    fun iceCreamApi(retrofit: Retrofit) : IceCreamApi{
        return retrofit.create(IceCreamApi::class.java)
    }

    @Singleton
    @Provides
    fun pastaApi(retrofit: Retrofit) : PastaApi{
        return retrofit.create(PastaApi::class.java)
    }

    @Singleton
    @Provides
    fun quickApi(retrofit : Retrofit) : QuickDeliveryApi{
        return retrofit.create(QuickDeliveryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): OrderDatabase {
        return Room.databaseBuilder(
            context,
            OrderDatabase::class.java,
            "order_database"
        ).build()
    }

}