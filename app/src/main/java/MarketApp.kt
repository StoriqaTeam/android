import android.app.Application
import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

class MarketApp : Application() {
    lateinit var client: ApolloClient

    override fun onCreate() {
        super.onCreate()

        client = setupApollo()

    }

    private fun setupApollo(): ApolloClient {
        val okHttp = OkHttpClient
                .Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val builder = original.newBuilder().method(original.method(),
                            original.body())
                    builder.addHeader("Authorization", token)
                    chain.proceed(builder.build())
                }
                .build()
        return ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttp)
                .build()
    }

    //todo getting token
    private val BASE_URL = "https://nightly.stq.cloud:60443/graphql"
    private val token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJleHAiOjE1MzQ5MjI5MzIsInByb3ZpZGVyIjoiRW1haWwifQ.VT7XzvgvzQdQ4YtHaYh7j6xqEt76Z1ltawe7O9rSxz0YUca7jZsJk-zYDy42R3aSOf7YFjSRgmwB2PoZcz7muFz_Vdkmxi4V_NrHg4ekBwsvuKg32TcOR12xcqAvuo6osJQmqa5ld7zbp_r_ZReFZDwxiJPkmHRjRTmj_zjGI2GT4JeKYWaUSimSGwn0bZvI-Yxh7icVAFWvDASTA0KXnn_a8vtPif1T759mX4pVrOyA4MYcB6DsUa2r2kYt7c-oqJP1cDTNic3RSXJsPJOEXH6ylfd_oLgXNVZCTyd94XNQLJOvDEr5a9jjD2j3iO9Zjvv-qdTUAjL5FXWWxOFNAQ"

}