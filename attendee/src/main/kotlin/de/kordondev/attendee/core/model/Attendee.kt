package de.kordondev.attendee.core.model

data class NewAttendee(
        val firstName: String,
        val lastName: String,
        val department: Department
)

data class Attendee(
        val id: Long,
        val firstName: String,
        val lastName: String,
        val department: Department
)
