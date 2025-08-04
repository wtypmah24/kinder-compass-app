package com.example.school_companion.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class MonitoringData(
    val id: String,
    val childId: String,
    val date: String,
    val emotionalState: EmotionalState = EmotionalState.NEUTRAL,
    val behaviorPattern: BehaviorPattern = BehaviorPattern.NORMAL,
    val learningProgress: LearningProgress = LearningProgress.ON_TRACK,
    val socialInteraction: SocialInteraction = SocialInteraction.GOOD,
    val notes: String = "",
    val companionId: String
) : Parcelable

enum class EmotionalState {
    VERY_HAPPY, HAPPY, NEUTRAL, SAD, VERY_SAD, ANXIOUS, EXCITED
}

enum class BehaviorPattern {
    EXCELLENT, GOOD, NORMAL, CHALLENGING, DIFFICULT
}

enum class LearningProgress {
    EXCELLENT, GOOD, ON_TRACK, NEEDS_SUPPORT, STRUGGLING
}

enum class SocialInteraction {
    EXCELLENT, GOOD, AVERAGE, LIMITED, DIFFICULT
} 