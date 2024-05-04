package com.yapp.domain.usecases

import java.time.LocalDateTime
import javax.inject.Inject

class GetCurrentTimeUseCase @Inject constructor() {

    operator fun invoke(): LocalDateTime {
        return LocalDateTime.now()
    }

}
