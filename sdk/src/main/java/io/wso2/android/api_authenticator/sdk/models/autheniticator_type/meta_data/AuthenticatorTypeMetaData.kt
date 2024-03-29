package io.wso2.android.api_authenticator.sdk.models.autheniticator_type.meta_data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.wso2.android.api_authenticator.sdk.util.JsonUtil

/**
 * Meta data related to the authenticator type
 */
@JsonIgnoreProperties(ignoreUnknown = true)
open class AuthenticatorTypeMetaData(
    /**
     * I18n key related to the authenticator type
     */
    open val i18nKey: String? = null,
    /**
     * Prompt type
     */
    open val promptType: String? = null,
    /**
     * Params related to the authenticator type
     */
    open val params: ArrayList<AuthenticatorTypeParam>? = null,
    /**
     * Additional data related to the authenticator type
     */
    open val additionalData: AuthenticatorTypeAdditionalData? = null
) {
    /**
     * Parameters related to the authenticator type
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    open class AuthenticatorTypeParam(
        open val param: String? = null,
        open val type: String? = null,
        open val order: Int? = null,
        open val i18nKey: String? = null,
        open val displayName: String? = null,
        open val confidential: Boolean? = null
    )

    /**
     * Additional data related to the authenticator type
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    open class AuthenticatorTypeAdditionalData(
        open val nonce: String? = null,
        open val clientId: String? = null,
        open val scope: String? = null,
        open val challengeData: String? = null,
        open val state: String? = null,
        open val redirectUrl: String? = null
    )

    fun toJsonString(): String {
        return JsonUtil.getJsonString(this)
    }
}
