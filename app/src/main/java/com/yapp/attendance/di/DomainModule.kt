package com.yapp.attendance.di

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.model.SessionEntity
import com.yapp.domain.model.TeamEntity
import com.yapp.domain.usecases.GetSessionListUseCase
import com.yapp.domain.usecases.GetTeamListUseCase
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.TaskResult
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow


@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun provideGetTeamListUseCase(
        getTeamListUseCase: GetTeamListUseCase
    ): BaseUseCase<@JvmSuppressWildcards Flow<TaskResult<List<TeamEntity>>>, Unit>

    @Binds
    abstract fun provideGetSessionListUseCase(
        getSessionListUseCase: GetSessionListUseCase
    ): BaseUseCase<@JvmSuppressWildcards Flow<TaskResult<List<SessionEntity>>>, Unit>

}