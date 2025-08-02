package core.datastore

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SettingsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlin.reflect.KProperty

private val scope = CoroutineScope(Dispatchers.Main)

private inline fun <T> createStateFlow(
    getValue: () -> T,
    crossinline addListener: (callback: (T) -> Unit) -> SettingsListener,
): StateFlow<T> = callbackFlow {
    val listener = addListener {
        trySend(it)
    }
    awaitClose {
        listener.deactivate()
    }
}.stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(),
    initialValue = getValue(),
)

internal class SettingState<T>(
    private val setValue: (T) -> Unit,
    val addListener: (callback: (T) -> Unit) -> SettingsListener,
    private val getValue: () -> T,
) : StateFlow<T> by createStateFlow(getValue, addListener) {
    operator fun getValue(any: Any, property: KProperty<*>): T {
        return getValue()
    }

    operator fun setValue(any: Any, property: KProperty<*>, value: T) {
        setValue(value)
    }
}

internal fun <T> createState(
    key: String,
    defaultValue: T,
    updatePreference: (String, T) -> Unit,
    addListener: (String, T, callback: (T) -> Unit) -> SettingsListener,
    getValue: (String, T) -> T?,
) = SettingState(
    setValue = { updatePreference(key, it ?: defaultValue) },
    addListener = { addListener(key, defaultValue, it) },
    getValue = { getValue(key, defaultValue) ?: defaultValue },
)

private fun <T> ObservableSettings.createNullableState(
    key: String,
    updatePreference: (String, T) -> Unit,
    addListener: (String, callback: (T?) -> Unit) -> SettingsListener,
    getValue: (String) -> T?,
) = SettingState(
    setValue = { value -> value?.also { updatePreference(key, it) } ?: remove(key) },
    addListener = { addListener(key, it) },
    getValue = { getValue(key) },
)

@Suppress("UNCHECKED_CAST")
internal inline fun <reified T> ObservableSettings.value(
    key: String,
    defaultValue: T
): SettingState<T> =
    when (T::class) {
        Int::class -> createState(key, defaultValue as Int, ::putInt, ::addIntListener, ::getInt)
        Long::class -> createState(
            key,
            defaultValue as Long,
            ::putLong,
            ::addLongListener,
            ::getLong
        )

        Boolean::class -> createState(
            key,
            defaultValue as Boolean,
            ::putBoolean,
            ::addBooleanListener,
            ::getBoolean
        )

        String::class -> createState(
            key,
            defaultValue as String,
            ::putString,
            ::addStringListener,
            ::getString
        )

        Float::class -> createState(
            key,
            defaultValue as Float,
            ::putFloat,
            ::addFloatListener,
            ::getFloat
        )

        else -> error("unsupported type '${T::class.simpleName}'")
    } as SettingState<T>

@Suppress("UNCHECKED_CAST")
internal inline fun <reified T> ObservableSettings.value(key: String): SettingState<T?> =
    when (T::class) {
        Int::class -> createNullableState(key, ::putInt, ::addIntOrNullListener, ::getIntOrNull)
        Long::class -> createNullableState(key, ::putLong, ::addLongOrNullListener, ::getLongOrNull)
        Boolean::class -> createNullableState(
            key,
            ::putBoolean,
            ::addBooleanOrNullListener,
            ::getBooleanOrNull
        )

        String::class -> createNullableState(
            key,
            ::putString,
            ::addStringOrNullListener,
            ::getStringOrNull
        )

        Float::class -> createNullableState(
            key,
            ::putFloat,
            ::addFloatOrNullListener,
            ::getFloatOrNull
        )

        else -> error("unsupported type '${T::class.simpleName}'")
    } as SettingState<T?>
