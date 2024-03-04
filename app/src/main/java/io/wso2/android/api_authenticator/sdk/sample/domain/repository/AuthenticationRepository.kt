package io.wso2.android.api_authenticator.sdk.sample.domain.repository

import io.wso2.android.api_authenticator.sdk.sample.domain.model.error.AuthenticationError
import android.content.Context
import arrow.core.Either
import io.wso2.android.api_authenticator.sdk.models.auth_params.AuthParams
import io.wso2.android.api_authenticator.sdk.models.autheniticator_type.AuthenticatorType
import io.wso2.android.api_authenticator.sdk.models.authorize_flow.AuthorizeFlow

interface AuthenticationRepository {
    suspend fun authorize(): Either<AuthenticationError, AuthorizeFlow>

    suspend fun authenticate(
        authenticatorType: AuthenticatorType,
        authenticatorParameters: AuthParams,
    ): Either<AuthenticationError, AuthorizeFlow>

    suspend fun getAccessToken(
        context: Context,
        authorizationCode: String
    ): Either<AuthenticationError, String>
}