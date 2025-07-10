package core.models

enum class ErrorCode {
    Unknown,
    InvalidRequest,
    NotFound,
    UnableToParseResponse,

    InvalidEmail,
    EmailAlreadyExists,
    PasswordTooShort,
    PasswordTooLong,
    UserNotFoundOrInvalidPassword,
    InvalidRefreshToken,
    RefreshTokenExpired,
}
