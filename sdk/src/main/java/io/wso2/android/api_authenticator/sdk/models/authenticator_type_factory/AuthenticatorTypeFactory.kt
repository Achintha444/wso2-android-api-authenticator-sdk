package io.wso2.android.api_authenticator.sdk.models.authenticator_type_factory

import io.wso2.android.api_authenticator.sdk.models.autheniticator_type.AuthenticatorType
import io.wso2.android.api_authenticator.sdk.models.autheniticator_type.BasicAuthenticatorType
import io.wso2.android.api_authenticator.sdk.models.autheniticator_type.GoogleAuthenticatorType
import io.wso2.android.api_authenticator.sdk.models.autheniticator_type.PasskeyAuthenticatorType
import io.wso2.android.api_authenticator.sdk.models.autheniticator_type.TotpAuthenticatorType
import io.wso2.android.api_authenticator.sdk.models.meta_data.AuthenticatorTypeMetaData
import io.wso2.android.api_authenticator.sdk.models.meta_data.BasicAuthenticatorTypeMetaData
import io.wso2.android.api_authenticator.sdk.models.meta_data.GoogleAuthenticatorTypeMetaData
import io.wso2.android.api_authenticator.sdk.models.meta_data.PasskeyAuthenticatorTypeMetaData
import io.wso2.android.api_authenticator.sdk.models.meta_data.TotpAuthenticatorTypeMetaData

/**
 * Authenticator type factory
 */
class AuthenticatorTypeFactory {

    companion object {

        /**
         * Get authenticator type from authenticator id, authenticator name, identity provider, metadata and required parameters
         *
         * @param authenticatorId Authenticator id
         * @param authenticator Authenticator name
         * @param idp Identity provider
         * @param metadata Metadata of the authenticator type
         * @param requiredParams Required parameters of the authenticator type
         * @return Authenticator type
         */
        fun getAuthenticatorType(
            authenticatorId: String,
            authenticator: String,
            idp: String,
            metadata: AuthenticatorTypeMetaData?,
            requiredParams: List<String>?
        ): AuthenticatorType {
            return when (authenticator) {
                BasicAuthenticatorType.AUTHENTICATOR_TYPE -> {
                    val authenticatorTypeMetaData = BasicAuthenticatorTypeMetaData(
                        metadata!!.promptType,
                        metadata.params
                    )
                    BasicAuthenticatorType(
                        authenticatorId,
                        authenticator,
                        idp,
                        authenticatorTypeMetaData,
                        requiredParams
                    )
                }

                GoogleAuthenticatorType.AUTHENTICATOR_TYPE -> {
                    val authenticatorTypeMetaData = GoogleAuthenticatorTypeMetaData(
                        metadata!!.i18nKey,
                        metadata!!.promptType,
                        metadata!!.additionalData as GoogleAuthenticatorTypeMetaData.GoogleAdditionalData
                    )
                    GoogleAuthenticatorType(
                        authenticatorId,
                        authenticator,
                        idp,
                        authenticatorTypeMetaData,
                        requiredParams
                    )
                }

                PasskeyAuthenticatorType.AUTHENTICATOR_TYPE -> {
                    val authenticatorTypeMetaData = PasskeyAuthenticatorTypeMetaData(
                        metadata!!.i18nKey,
                        metadata!!.promptType,
                        metadata!!.additionalData as PasskeyAuthenticatorTypeMetaData.PasskeyAdditionalData
                    )
                    PasskeyAuthenticatorType(
                        authenticatorId,
                        authenticator,
                        idp,
                        authenticatorTypeMetaData,
                        requiredParams
                    )
                }

                TotpAuthenticatorType.AUTHENTICATOR_TYPE -> {
                    val authenticatorTypeMetaData = TotpAuthenticatorTypeMetaData(
                        metadata!!.i18nKey!!,
                        metadata!!.promptType,
                        metadata!!.params
                    )
                    TotpAuthenticatorType(
                        authenticatorId,
                        authenticator,
                        idp,
                        authenticatorTypeMetaData,
                        requiredParams
                    )
                }

                else -> {
                    AuthenticatorType(authenticatorId, authenticator, idp, metadata, requiredParams)
                }
            }
        }
    }
}
