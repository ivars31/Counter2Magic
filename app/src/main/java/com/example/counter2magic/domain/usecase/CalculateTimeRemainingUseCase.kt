package com.example.counter2magic.domain.usecase

import com.example.counter2magic.domain.model.CountdownEvent
import com.example.counter2magic.domain.model.TimeRemaining
import java.time.Instant
import javax.inject.Inject

class CalculateTimeRemainingUseCase @Inject constructor() {

    operator fun invoke(event: CountdownEvent, now: Instant = Instant.now()): TimeRemaining {
        val secondsRemaining = event.targetDateTime.epochSecond - now.epochSecond
        return TimeRemaining.fromSeconds(secondsRemaining)
    }
}
