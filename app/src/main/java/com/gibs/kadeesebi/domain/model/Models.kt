package com.gibs.kadeesebi.domain.model

data class EventType(
    val id: String,
    val name: String,
    val iconKey: String,
    val builtInKey: String? = null,
    val sortOrder: Int = 0,
)

object BuiltInEventTypes {
    const val BIRTHDAY = "birthday"
    const val NEW_YEAR = "new_year"
    const val OTHER = "other"
}

enum class GiftDirection {
    RECEIVED,
    GIVEN
}

data class Circle(
    val id: String,
    val name: String,
    val colorArgb: Int,
    val builtInKey: String? = null,
)

object BuiltInCircles {
    const val FRIEND = "friend"
    const val COLLEAGUE = "colleague"
    const val RELATIVE = "relative"
}

data class Person(
    val id: String,
    val fullName: String,
    val phone: String? = null,
    val photoUri: String? = null,
    val circleId: String? = null,
    val note: String? = null,
    val relation: String? = null,
)

data class Toi(
    val id: String,
    val hostPersonId: String?,
    val eventTypeId: String,
    val date: Long,
    val place: String? = null,
    val isOwnToi: Boolean,
    val note: String? = null,
    val title: String? = null,
    val reminderAt: Long? = null,
)

data class Gift(
    val id: String,
    val toiId: String,
    val personId: String,
    val direction: GiftDirection,
    val amount: Long,
    val currency: String = "KZT",
    val date: Long,
    val note: String? = null,
    val photoUri: String? = null,
)

data class Balance(
    val personId: String,
    val received: Long,
    val given: Long,
    val net: Long,
    val suggested: Long,
)
