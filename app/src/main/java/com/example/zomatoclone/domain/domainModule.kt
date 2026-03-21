package com.example.zomatoclone.domain

import android.content.Context
import com.example.zomatoclone.data.UserDataStore
import com.example.zomatoclone.data.repoImpl.AllCategoryRepoImpl
import com.example.zomatoclone.data.repoImpl.BiryaniRepoImpl
import com.example.zomatoclone.data.repoImpl.BurgerRepoImpl
import com.example.zomatoclone.data.repoImpl.ChineseRepoImpl
import com.example.zomatoclone.data.repoImpl.IceCreamRepoImpl
import com.example.zomatoclone.data.repoImpl.PastaRepoImpl
import com.example.zomatoclone.data.repoImpl.PizzaRepoImpl
import com.example.zomatoclone.data.repoImpl.QuickDeliveryRepoImpl
import com.example.zomatoclone.data.repoImpl.RepoImpl
import com.example.zomatoclone.data.repoImpl.RollsRepoImpl
import com.example.zomatoclone.data.repoImpl.SweetsRepoImpl
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
import com.example.zomatoclone.domain.repo.AllCategoryRepo
import com.example.zomatoclone.domain.repo.BiryaniRepo
import com.example.zomatoclone.domain.repo.BurgerRepo
import com.example.zomatoclone.domain.repo.ChineseRepo
import com.example.zomatoclone.domain.repo.IceCreamRepo
import com.example.zomatoclone.domain.repo.PastaRepo
import com.example.zomatoclone.domain.repo.PizzaRepo
import com.example.zomatoclone.domain.repo.QuickDeliveryRepo
import com.example.zomatoclone.domain.repo.Repo
import com.example.zomatoclone.domain.repo.RollsRepo
import com.example.zomatoclone.domain.repo.SweetsRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object domainModule {
    @Provides
    @Singleton
    fun provideUserDataStore(
        @ApplicationContext context: Context
    ): UserDataStore {
        return UserDataStore(context)
    }

    @Provides
    fun provideFire(firebaseAuth : FirebaseAuth, firebaseStore :FirebaseFirestore): Repo{

            return RepoImpl(firebaseAuth,firebaseStore)

    }

    @Provides
    fun providePizza(api: RestaurantApi) : PizzaRepo {
        return PizzaRepoImpl(api)
    }

    @Provides
    fun provideChinese(api: ChineseApi): ChineseRepo{
        return ChineseRepoImpl(api)
    }

    @Provides
    fun provideAll(api : AllCategoryApi): AllCategoryRepo{
        return AllCategoryRepoImpl(api)
    }


    @Provides
    fun provieBurger(api : BurgersApi) : BurgerRepo {
        return BurgerRepoImpl(api)
    }

    @Provides
    fun provideBiryani(api : BiryaniApi) : BiryaniRepo{
        return BiryaniRepoImpl(api)
    }

    @Provides
    fun provideRolls(api: RollsApi) : RollsRepo{
        return RollsRepoImpl(api)

    }

    @Provides
    fun provideSweets(api: SweetsApi) : SweetsRepo{
        return SweetsRepoImpl(api)
    }

    @Provides
    fun providePasta(api : PastaApi) : PastaRepo{
        return PastaRepoImpl(api)
    }

    @Provides
    fun orovideIceCream(api : IceCreamApi) : IceCreamRepo{
        return IceCreamRepoImpl(api)
    }

    @Provides
    fun provideQuickDelivery(api : QuickDeliveryApi) : QuickDeliveryRepo{
        return QuickDeliveryRepoImpl(api)
    }


}