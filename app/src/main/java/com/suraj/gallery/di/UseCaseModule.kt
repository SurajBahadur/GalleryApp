package com.suraj.gallery.di

import com.suraj.gallery.domain.repository.MediaRepository
import com.suraj.gallery.domain.usecase.GetAlbumsUseCase
import com.suraj.gallery.domain.usecase.GetMediaItemsUseCase
import com.suraj.gallery.domain.usecase.UseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by Suraj Bahadur on 6/5/2025.
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideUseCases(repoRepository: MediaRepository): UseCases {
        return UseCases(
            getAlbumsUseCase = GetAlbumsUseCase(repoRepository),
            getMediaItemsUseCase = GetMediaItemsUseCase(repoRepository)
        )
    }
}