package ru.nikitazar.santehnika_online.hiltModules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nikitazar.santehnika_online.repository.PointRepository
import ru.nikitazar.santehnika_online.repository.PointRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PointRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPointRepository(impl: PointRepositoryImpl): PointRepository
}