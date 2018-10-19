package net.suntrans.powerpeace.api;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.powerpeace.App;
import net.suntrans.powerpeace.converter.MyGsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Looney on 2016/12/15.
 */

public class RetrofitHelper {
    private static final int DEFAULT_TIMEOUT = 10;
    //UnknownHostException
    public static String BASE_URL = "http://gszydx.suntrans-cloud.com:7088/";
//  public static String BASE_URL = "http://gszynw.suntrans-cloud.com:80/";
//  public static String BASE_URL_INNER = "http://gszynw.suntrans-cloud.com:80/";
    public static boolean INNER = false;
    private static OkHttpClient mOkHttpClient;
    private static Api api = null;

    static {
        INNER = App.getSharedPreferences().getBoolean("inner", false);
        initOkHttpClient();
    }

    public static Api getApi() {

//        if (INNER) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL_INNER)
//                    .client(mOkHttpClient)
//                    .addConverterFactory(MyGsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                    .build();
//        } else {
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(mOkHttpClient)
                    .addConverterFactory(MyGsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
//        }
            api = retrofit.create(Api.class);
        }

        return api;
    }

    public static Api getYichangApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(Api.class);
    }

    public static Api getLoginApi() {
        Retrofit retrofit = null;
//        if (INNER) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL_INNER)
//                    .client(mOkHttpClient)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                    .build();
//        } else {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
//        }

        return retrofit.create(Api.class);
    }


    private static void initOkHttpClient() {
//        System.out.println("wobeizhixingle=======================");
        Interceptor netInterceptor =
                new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        String header = App.getSharedPreferences().getString("token", "-1");
//                        System.out.println(header);
//                        System.out.println(header.length());
                        Request original = chain.request();

                        RequestBody newBody = original.body();

//                        if (original.body() instanceof FormBody) {
//                            newBody = addParamsToFormBody((FormBody) original.body());
//                        } else {
//                            newBody =    addParamsToFormBody();
//                        }

                        Request newRequest = original.newBuilder()
                                .header("Authorization", "Bearer " + header)
                                .method(original.method(), newBody)
                                .build();

                        return chain.proceed(newRequest);
                    }
                };

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (mOkHttpClient == null) {
            synchronized (RetrofitHelper.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(netInterceptor)
//                            .addInterceptor(logging)
                            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    /**
     * 为FormBody类型请求体添加参数
     *
     * @param body
     * @return
     */
    private static FormBody addParamsToFormBody(FormBody body) {
        FormBody.Builder builder = new FormBody.Builder();
        String header = App.getSharedPreferences().getString("token", "raVnKIh8Rv");
//        String group = App.getSharedPreferences().getString("group", "raVnKIh8Rv");
//        String id = App.getSharedPreferences().getString("id", "-1");
//        LogUtil.i("token", header);
        builder.add("token", header);
//        builder.add("group", group);
//        builder.add("id", id);
        //添加原请求体
        for (int i = 0; i < body.size(); i++) {
            builder.addEncoded(body.encodedName(i), body.encodedValue(i));
        }
        return builder.build();
    }

    /**
     * 为FormBody类型请求体添加参数
     *
     * @return
     */
    private static FormBody addParamsToFormBody() {
        FormBody.Builder builder = new FormBody.Builder();
        String header = App.getSharedPreferences().getString("token", "raVnKIh8Rv");
//        String group = App.getSharedPreferences().getString("group", "raVnKIh8Rv");
//        String id = App.getSharedPreferences().getString("id", "-1");
        LogUtil.i("token", header);
        builder.add("token", header);
//        builder.add("group", group);
//        builder.add("id", id);

        return builder.build();
    }


}
