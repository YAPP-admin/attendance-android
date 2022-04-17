package com.yapp.domain.usecases

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.util.BaseUseCase
import com.yapp.domain.util.DateUtil
import com.yapp.domain.util.DispatcherProvider
import com.yapp.domain.util.TaskResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class CheckQrAuthTime @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    coroutineDispatcher: DispatcherProvider
) : BaseUseCase<Flow<TaskResult<Boolean>>, Unit>(coroutineDispatcher) {

    companion object {
        private const val BEFORE_5_MINUTE = -5
        private const val AFTER_30_MINUTE = 30
    }

    override suspend fun invoke(params: Unit?): Flow<TaskResult<Boolean>> {
        return firebaseRemoteConfig.getSessionList()
            .map { list ->
                val upComingSession = list.firstOrNull { DateUtil.isUpcomingSession(it.date) }
                val elapsedTime = DateUtil.getElapsedTime(upComingSession!!.date)

                return@map elapsedTime in BEFORE_5_MINUTE..AFTER_30_MINUTE
            }.toResult()
    }

}