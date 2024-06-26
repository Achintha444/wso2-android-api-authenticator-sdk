package io.wso2.android.api_authenticator.sdk.core.managers.flow

import com.fasterxml.jackson.databind.JsonNode
import io.wso2.android.api_authenticator.sdk.models.authentication_flow.AuthenticationFlow
import io.wso2.android.api_authenticator.sdk.models.authentication_flow.AuthenticationFlowNotSuccess
import io.wso2.android.api_authenticator.sdk.models.authentication_flow.AuthenticationFlowSuccess
import io.wso2.android.api_authenticator.sdk.models.exceptions.AuthenticatorException
import io.wso2.android.api_authenticator.sdk.models.exceptions.FlowManagerException

/**
 * Flow manager interface
 * This interface is responsible for handling the state of the authorization flow.
 */
internal interface FlowManager {
    /**
     * Set the flow id of the authorization flow
     *
     * @param flowId Flow id of the authorization flow
     */
    fun setFlowId(flowId: String)

    /**
     * Get the flow id of the authorization flow
     *
     * @return Flow id of the authorization flow
     */
    fun getFlowId(): String

    /**
     * Manage the state of the authorization flow.
     * This function will return the [AuthenticationFlowNotSuccess] if the flow is incomplete.
     * This function will return the [AuthenticationFlowSuccess] if the flow is completed.
     * This function will throw an [AuthenticatorException] if the flow is failed.
     *
     * @param responseObject Response object of the authorization request
     *
     * @return [AuthenticationFlow] with the authenticators in the next step
     * @throws [AuthenticatorException] If the flow is failed
     * @throws [FlowManagerException] If the flow is failed incomplete
     */
    fun manageStateOfAuthorizeFlow(responseObject: JsonNode): AuthenticationFlow

    /**
     * Remove the instance of the [FlowManager]
     */
    fun dispose()
}
