package de.kordondev.attendee.core.service

import de.kordondev.attendee.core.model.Attendee
import de.kordondev.attendee.core.model.Department
import de.kordondev.attendee.core.model.NewAttendee
import de.kordondev.attendee.core.persistence.entry.AttendeeEntry
import de.kordondev.attendee.core.persistence.entry.DepartmentEntry
import de.kordondev.attendee.core.persistence.entry.Roles
import de.kordondev.attendee.core.persistence.repository.AttendeeRepository
import de.kordondev.attendee.core.security.AuthorityService
import de.kordondev.attendee.exception.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AttendeeService (
        private val attendeeRepository: AttendeeRepository,
        private val authorityService: AuthorityService
) {
    fun getAttendees() : Iterable<Attendee> {
        return attendeeRepository.findAll()
                .map { AttendeeEntry.to(it) }
                .filter { authorityService.hasAuthorityFilter(it, listOf(Roles.ADMIN, Roles.SPECIALIZED_FIELD_DIRECTOR)) }
    }

    fun getAttendee(id: Long) : Attendee {
        return attendeeRepository
                .findByIdOrNull(id)
                ?.let { AttendeeEntry.to(it) }
                ?.let { authorityService.hasAuthority(it, listOf(Roles.ADMIN, Roles.SPECIALIZED_FIELD_DIRECTOR)) }
            ?: throw NotFoundException("Attendee with id $id not found")
    }

    fun createAttendee(attendee: NewAttendee) : Attendee {
        authorityService.hasAuthority(attendee, listOf(Roles.ADMIN, Roles.SPECIALIZED_FIELD_DIRECTOR))
        return attendeeRepository
                .save(AttendeeEntry.of(attendee))
                .let { savedAttendee -> AttendeeEntry.to(savedAttendee) }
    }

    fun saveAttendee(id: Long, attendee: NewAttendee): Attendee {
        authorityService.hasAuthority(attendee, listOf(Roles.ADMIN, Roles.SPECIALIZED_FIELD_DIRECTOR))
        return attendeeRepository
                .save(AttendeeEntry.of(attendee, id))
                .let { savedAttendee -> AttendeeEntry.to(savedAttendee) }
    }

    fun deleteAttendee(id: Long) {
         attendeeRepository.findByIdOrNull(id)
                 ?.let { AttendeeEntry.to(it) }
                 ?.let { authorityService.hasAuthority(it, listOf(Roles.ADMIN, Roles.SPECIALIZED_FIELD_DIRECTOR)) }
                 ?.let { AttendeeEntry.of(it) }
                 ?.let { attendeeRepository.delete(it) }
             ?: throw NotFoundException("Attendee with id $id not found and therefore not deleted")
    }

    fun getAttendeesForDepartment(department: Department): List<Attendee> {
        return attendeeRepository
                .findByDepartment(DepartmentEntry.of(department))
                .map{ attendee -> AttendeeEntry.to(attendee) }
                .filter { authorityService.hasAuthorityFilter(it, listOf(Roles.ADMIN, Roles.SPECIALIZED_FIELD_DIRECTOR)) }
    }
}