package com.hoanmy.kleanco.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import rx.android.BuildConfig;


public class RequestApi {
    private static Api api;

    public static Api getInstance() {
        if (api == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(interceptor);

            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                httpClient.sslSocketFactory(sslSocketFactory);
                httpClient.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            api = new Retrofit.Builder()
                    .baseUrl("https://hoanmy-api.tieuphuyeucay.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(httpClient.build())
                    .build().create(Api.class);
        }

        return api;
    }


    public interface Api {
        @Headers("Content-Type: application/json")
        @POST("/login")
        Observable<JsonElement> postLogin(@Body JsonObject body);

        @Headers("Content-Type: application/json")
        @POST("/task/feedback")
        Observable<JsonElement> postFeedback(@Query("token") String token, @Body JsonObject body);

        @Headers("Content-Type: application/json")
        @POST("/task/complete")
        Observable<JsonElement> postDoneJob(@Query("token") String token, @Body JsonObject body);

        @Headers("Content-Type: application/json")
        @POST("/task/extra/start")
        Observable<JsonElement> postExtraJobStart(@Query("token") String token, @Body JsonObject body);

        @Headers("Content-Type: application/json")
        @POST("/task/extra/end")
        Observable<JsonElement> postExtraJobEnd(@Query("token") String token, @Body JsonObject body);

        @Headers("Content-Type: application/json")
        @POST("/task/pause/start")
        Observable<JsonElement> postPauseJobStart(@Query("token") String token, @Body JsonObject body);

        @Headers("Content-Type: application/json")
        @POST("/task/pause/end")
        Observable<JsonElement> postPauseJobEnd(@Query("token") String token, @Body JsonObject body);

        @GET("/project/detail")
        Observable<JsonElement> getDetailProject(@Query("token") String token);

        @GET("/project")
        Observable<JsonElement> getProjects(@Query("token") String token);

        @GET("/project/staff")
        Observable<JsonElement> getEmployees(@Query("token") String token, @Query("project_id") String project_id);

        @GET("/project/task")
        Observable<JsonElement> getProjectForId(@Query("token") String token, @Query("status") String status, @Query("date") String date, @Query("limit") int limit);


        @GET("/project/task")
        Observable<JsonElement> getProjectForFeedback(@Query("token") String token, @Query("status") String status, @Query("date") String date, @Query("feedback_status") int feedback);

    }
}
