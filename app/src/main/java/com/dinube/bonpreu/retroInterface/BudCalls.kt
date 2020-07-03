package com.dinube.bonpreu.retroInterface

import com.dinube.bonpreu.data.Authentication
import com.dinube.bonpreu.data.ConnectBankResponse
import com.dinube.bonpreu.data.accounts.AccountsResponse
import com.dinube.bonpreu.data.saltedgedata.connection.ConnectionUrlRequest
import com.dinube.bonpreu.data.saltedgedata.connection.ConnectionUrlResponse
import com.dinube.bonpreu.data.saltedgedata.connection.FetchConnectionsResponse
import com.dinube.bonpreu.data.saltedgedata.customer.createcustomer.CustomerCreationResponse
import com.dinube.bonpreu.data.saltedgedata.customer.createcustomer.CustomerCreationRequest
import com.dinube.bonpreu.data.saltedgedata.payment.paywithconnect.request.ConnectPayRequest
import com.dinube.bonpreu.data.saltedgedata.payment.paywithconnect.response.ConnectPayResponse
import com.dinube.bonpreu.data.saltedgedata.payment.paywithcreds.PayWithCredsResponse
import com.dinube.bonpreu.data.saltedgedata.payment.paywithcreds.request.PayWithCredsRequest
import com.dinube.bonpreu.data.saltedgedata.payment.paywithdirectapi.request.DirectApiPaymentRequest
import com.dinube.bonpreu.data.saltedgedata.payment.paywithdirectapi.response.DirectPayResponse
import com.dinube.bonpreu.data.saltedgedata.transactions.UserTransactionResponse
import com.dinube.bonpreu.data.transactions.TransactionResponse
import com.dinube.bonpreu.data.tspconnection.taskid.TspTaskIdResponse
import com.dinube.bonpreu.data.tspconnection.tspurl.TspUrlResponse
import com.dinube.bonpreu.data.tsppaymenturl.TspPaymentUrl
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface BudCalls {

    @FormUrlEncoded
    @POST("/v1/oauth/token")
    fun getAccessToken(@Header("Content-Type") header:String, @Field("grant_type") field:String): Call<Authentication>

    @Headers("Content-Type: application/json","X-Client-Id: c3d29c4c-dd10-4e4e-83fc-4cf85005184a", "X-Customer-Id: 234a3883-5997-46c7-800e-c55c67beca9a")
    @POST("/v1/open-banking/authorisation-gateway-url")
    fun getBankConnectUrl(@Header("Authorization") token: String,@Body rq: RequestBody): Call<ConnectBankResponse>


    @Headers("Content-Type: application/json","X-Client-Id: c3d29c4c-dd10-4e4e-83fc-4cf85005184a", "X-Customer-Id: 234a3883-5997-46c7-800e-c55c67beca9a")
    @POST("/v1/open-banking/authorisation-url")
    fun getTspTaskId(@Header("Authorization") token: String, @Body rq: RequestBody): Call<TspTaskIdResponse>

    @Headers("Content-Type: application/json","X-Client-Id: c3d29c4c-dd10-4e4e-83fc-4cf85005184a", "X-Customer-Id: 234a3883-5997-46c7-800e-c55c67beca9a")
    @GET("/v1/open-banking/authorisation-url/{task_id}")
    fun getTspUrl(@Header("Authorization") token: String, @Path("task_id") taskId: String): Call<TspUrlResponse>

    @Headers("Content-Type: application/json","X-Client-Id: c3d29c4c-dd10-4e4e-83fc-4cf85005184a", "X-Customer-Id: 234a3883-5997-46c7-800e-c55c67beca9a")
    @POST("/v1/payments/domestic/single")
    fun getTspPaymentUrl(@Header("Authorization") token: String, @Body rq: RequestBody): Call<TspPaymentUrl>

    @Headers("Content-Type: application/json","X-Client-Id: c3d29c4c-dd10-4e4e-83fc-4cf85005184a", "X-Customer-Id: 234a3883-5997-46c7-800e-c55c67beca9a","X-Customer-Secret: 0740fb5bab1df6f77b1fc840490e02ef5c2e3649c757f63b58ff79e16e9bcd02")
    @GET("/v1/open-banking/transactions")
    fun getUserTransactions(@Header("Authorization") token: String): Call<TransactionResponse>

    @Headers("Content-Type: application/json","X-Client-Id: c3d29c4c-dd10-4e4e-83fc-4cf85005184a", "X-Customer-Id: 234a3883-5997-46c7-800e-c55c67beca9a","X-Customer-Secret: 0740fb5bab1df6f77b1fc840490e02ef5c2e3649c757f63b58ff79e16e9bcd02")
    @GET("/v1/open-banking/accounts")
    fun getUserAccounts(@Header("Authorization") token: String): Call<AccountsResponse>

    @Headers("Content-Type: application/json","Accept: application/json", "App-id: v0X_ICZORL49VcKKeRFBM9Gr-_t5iILZCs8-R-qaOy8","Secret: FNBLNKlEBKy0C5cNRR6oM1c4CUrwPKEHt0f-ItgTlQI" )
    @POST
    fun getConnectionUrl(@Url url:String, @Body rq: ConnectionUrlRequest): Call<ConnectionUrlResponse>


    @Headers("Content-Type: application/json","Accept: application/json", "App-id: v0X_ICZORL49VcKKeRFBM9Gr-_t5iILZCs8-R-qaOy8","Secret: FNBLNKlEBKy0C5cNRR6oM1c4CUrwPKEHt0f-ItgTlQI" )
    @GET
    fun fetchConnections(@Url url:String, @Query("customer_id") customerId: String ): Call<FetchConnectionsResponse>

    @Headers("Content-Type: application/json","Accept: application/json", "App-id: v0X_ICZORL49VcKKeRFBM9Gr-_t5iILZCs8-R-qaOy8","Secret: FNBLNKlEBKy0C5cNRR6oM1c4CUrwPKEHt0f-ItgTlQI" )
    @POST
    fun createCustomer(@Url url:String, @Body customerCreationRequest: CustomerCreationRequest ): Call<CustomerCreationResponse>

    @Headers("Content-Type: application/json","Accept: application/json", "App-id: v0X_ICZORL49VcKKeRFBM9Gr-_t5iILZCs8-R-qaOy8","Secret: FNBLNKlEBKy0C5cNRR6oM1c4CUrwPKEHt0f-ItgTlQI" )
    @GET
    fun fetchSaltEdgeAccounts(@Url url:String, @Query("connection_id") connectionId: String ): Call<com.dinube.bonpreu.data.saltedgedata.accounts.AccountsResponse>

    @Headers("Content-Type: application/json","Accept: application/json", "App-id: v0X_ICZORL49VcKKeRFBM9Gr-_t5iILZCs8-R-qaOy8","Secret: FNBLNKlEBKy0C5cNRR6oM1c4CUrwPKEHt0f-ItgTlQI" )
    @GET
    fun fetchSaltEdgeTransactions(@Url url:String,@Query("account_id") accountId: String ,@Query("connection_id") connectionId: String ): Call<UserTransactionResponse>

    @Headers("Content-Type: application/json","Accept: application/json", "App-id: v0X_ICZORL49VcKKeRFBM9Gr-_t5iILZCs8-R-qaOy8","Secret: FNBLNKlEBKy0C5cNRR6oM1c4CUrwPKEHt0f-ItgTlQI" )
    @POST
    fun payWihCreds(@Url url:String, @Body payWithCredsRequest: PayWithCredsRequest): Call<PayWithCredsResponse>

    @Headers("Content-Type: application/json","Accept: application/json", "App-id: v0X_ICZORL49VcKKeRFBM9Gr-_t5iILZCs8-R-qaOy8","Secret: FNBLNKlEBKy0C5cNRR6oM1c4CUrwPKEHt0f-ItgTlQI" )
    @POST
    fun payWihDirectApi(@Url url:String, @Body payWithCredsRequest: DirectApiPaymentRequest): Call<DirectPayResponse>

    @Headers("Content-Type: application/json","Accept: application/json", "App-id: v0X_ICZORL49VcKKeRFBM9Gr-_t5iILZCs8-R-qaOy8","Secret: FNBLNKlEBKy0C5cNRR6oM1c4CUrwPKEHt0f-ItgTlQI" )
    @POST
    fun payWihDirectApiAuthorization(@Url url:String, @Body payWithCredsRequest: DirectApiPaymentRequest): Call<DirectPayResponse>

    @Headers("Content-Type: application/json","Accept: application/json", "App-id: v0X_ICZORL49VcKKeRFBM9Gr-_t5iILZCs8-R-qaOy8","Secret: FNBLNKlEBKy0C5cNRR6oM1c4CUrwPKEHt0f-ItgTlQI" )
    @POST
    fun payWithConnect(@Url url:String, @Body payWithCredsRequest: ConnectPayRequest): Call<ConnectPayResponse>

}
