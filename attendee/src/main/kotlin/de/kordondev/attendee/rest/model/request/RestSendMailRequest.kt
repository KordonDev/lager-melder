package de.kordondev.attendee.rest.model.request

import de.kordondev.attendee.core.model.SendTo
import javax.validation.constraints.NotBlank

data class RestSendMailRequest (
        @field:NotBlank(message = "sendTo cannot be blank")
        val sendTo: SendTo
)
