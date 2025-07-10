package core.resources

import core.resources.generated.resources.Res
import core.resources.generated.resources.operation_completed_successfully
import core.resources.generated.resources.you_are_not_authorized_to_perform_this_action


internal operator fun String.div(path: String): String {
    return "${removeSuffix("/")}/${path.removePrefix("/")}"
}


val RESPONSE_ERROR_MESSAGE_BY_STATUS
    get() = mapOf(
        "SUCCESS" to Res.string.operation_completed_successfully,
        "NOT_AUTHORIZED" to Res.string.you_are_not_authorized_to_perform_this_action,)
