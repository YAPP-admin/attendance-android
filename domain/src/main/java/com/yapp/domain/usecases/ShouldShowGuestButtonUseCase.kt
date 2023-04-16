package com.yapp.domain.usecases

import com.yapp.domain.firebase.FirebaseRemoteConfig
import com.yapp.domain.repository.RemoteConfigRepository
import javax.inject.Inject

/**
 * 스토어 게시를 위한 테스트 계정관련 remote config
 */
class ShouldShowGuestButtonUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository
) {

    suspend operator fun invoke(): Result<Boolean> {
        return remoteConfigRepository.shouldShowGuestButton()
    }

}
