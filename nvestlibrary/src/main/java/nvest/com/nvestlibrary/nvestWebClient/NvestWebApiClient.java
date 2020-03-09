package nvest.com.nvestlibrary.nvestWebClient;




import android.content.Context;
import android.text.TextUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import nvest.com.nvestlibrary.BuildConfig;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.logging.HttpLoggingInterceptor;

public class NvestWebApiClient {

    private static Retrofit retrofit = null;
    private static final String GENERAL_ERROR = "Some error occurred";
    private static final String SERVER_ERROR = "Some error occurred at server, Please try again later!!!";
    private static final String CONNECTION_ERROR = "Error while connecting to server. Please try again later";
    private static final String TIMEOUT_EXCEPTION = "Timeout exception";
    //public static final String BASE_URL = "http://bitoolmobileapi.beta.nvest.in/NSureServices.svc/";
    //public static final String BASE_URL = BuildConfig.BASE_URL;
//    public static final String BASE_URL_ZIP = BuildConfig.BASE_URL_ZIP;
    //public static final String BASE_URL = "https://api.myjson.com/bins/";
    private static OkHttpClient okHttpClient;

    /*public static Retrofit getClient() {
        if (retrofit==null) {

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG) {
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            } else {
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            }
            //loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.readTimeout(NvestLibraryConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS);
            client.connectTimeout(NvestLibraryConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS);
            client.writeTimeout(NvestLibraryConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS);
            //client.cache(null);
            client.addInterceptor(loggingInterceptor);


            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client.build())
                    //.addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }*/


    public static Retrofit getClient(){
        if (okHttpClient == null)
            initOkHttp();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.TEMP_BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static void initOkHttp() {
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(NvestLibraryConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(NvestLibraryConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(NvestLibraryConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(interceptor);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json");

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        okHttpClient = httpClient.build();
    }
}
