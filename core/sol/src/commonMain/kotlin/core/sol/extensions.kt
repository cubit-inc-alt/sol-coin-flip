package core.sol

import androidx.core.uri.Uri

fun Uri.queryParam(key: String): String? = getQueryParameters(key).firstOrNull()
