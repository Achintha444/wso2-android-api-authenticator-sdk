package io.wso2.android.api_authenticator.sdk.models.autheniticator_type

import io.wso2.android.api_authenticator.sdk.models.meta_data.AuthenticatorTypeMetaData

/**
 * AuthenticatorType model class
 */
open class AuthenticatorType(
    /**
     * Id of the authenticator type
     */
    open val authenticatorId: String,
    /**
     * Name of the authenticator type
     */
    open val authenticator: String,
    /**
     * Id of the idp of the authenticator type
     */
    open val idp: String,
    /**
     * Metadata of the authenticator type
     */
    open val metadata: AuthenticatorTypeMetaData?,
    /**
     * Required params that should be sent to the server for authentication in this authenticator type
     */
    open val requiredParams: List<String>?
)
