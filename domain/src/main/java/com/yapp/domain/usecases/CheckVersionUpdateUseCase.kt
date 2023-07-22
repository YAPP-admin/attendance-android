package com.yapp.domain.usecases

import com.yapp.domain.model.types.VersionType
import com.yapp.domain.repository.RemoteConfigRepository
import javax.inject.Inject

class CheckVersionUpdateUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
) {

    suspend operator fun invoke(currentVersionCode: Long): Result<VersionType> {
        return remoteConfigRepository.getVersionInfo().mapCatching { version ->
            val minVersionCode = version.minVersionCode
            val maxVersionCode = version.maxVersionCode

            return@mapCatching when {
                currentVersionCode < minVersionCode -> VersionType.REQUIRED
                currentVersionCode == maxVersionCode -> VersionType.NOT_REQUIRED
                else -> VersionType.UPDATED_BUT_NOT_REQUIRED
            }
        }
    }
}
