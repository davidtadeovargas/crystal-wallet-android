package cy.agorise.crystalwallet.apigenerator;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * This maanges the calls for the creation of accounts using the bitshares faucet
 *
 * Created by henry on 15/10/2017.
 */

public abstract class BitsharesFaucetApiGenerator {

    /**
     * Class to register a new Bitshares Account
     *
     * @param accountName The name of the Account to be register
     * @param ownerKey The owner key public address
     * @param activeKey The active key public address
     * @param memoKey the memo key public address
     * @param url The url of the faucet
     * @return The bitshares id of the registered account, or null
     */
    public static boolean registerBitsharesAccount(String accountName, String ownerKey,
                                                  String activeKey, String memoKey, String url){
        CreateAccountPetition petition = new CreateAccountPetition();
        final Account account = new Account();
        account.name=accountName;
        account.owner_key=ownerKey;
        account.active_key=activeKey;
        account.memo_key=memoKey;
        petition.account=account;
        Gson gson = new Gson();
        String jsonPetition = gson.toJson(petition);
        System.out.println("create account petition :" + jsonPetition);

        //TODO faucet function

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("name", account.name);
        hm.put("owner_key", account.owner_key);
        hm.put("active_key", account.active_key);
        hm.put("memo_key", account.memo_key);
        hm.put("refcode", "agorise");
        hm.put("referrer", "agorise");

        HashMap<String, HashMap> hashMap = new HashMap<>();
        hashMap.put("account", hm);
        final boolean[] answer = {false};
        final Object SYNC = new Object();
        try {
            ServiceGenerator sg = new ServiceGenerator(url);
            IWebService service = sg.getService(IWebService.class);
            final Call<RegisterAccountResponse> postingService = service.getReg(hashMap);
            postingService.enqueue(new Callback<RegisterAccountResponse>() {

                @Override
                public void onResponse(Call<RegisterAccountResponse> call, Response<RegisterAccountResponse> response) {
                    if (response.isSuccessful()) {
                        RegisterAccountResponse resp = response.body();
                        if (resp.account != null) {
                            try {
                                if(resp.account.name.equals(account.name)) {
                                    synchronized (SYNC){
                                        answer[0] = true;
                                        SYNC.notifyAll();
                                    }
                                }else{
                                   //ERROR
                                    synchronized (SYNC) {
                                        SYNC.notifyAll();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                synchronized (SYNC) {
                                    SYNC.notifyAll();
                                }
                            }
                        }else{
                            //ERROR
                            synchronized (SYNC) {
                                SYNC.notifyAll();
                            }

                        }
                    }else{
                        //ERROR
                        synchronized (SYNC) {
                            SYNC.notifyAll();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterAccountResponse> call, Throwable t) {
                    t.printStackTrace();
                    synchronized (SYNC) {
                        SYNC.notifyAll();
                    }
                }
            });
            synchronized (SYNC) {
                SYNC.wait(60000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return answer[0];
    }

    /**
     * Class used for the json serializer. this represents a peitition
     */
    private static class CreateAccountPetition{
        // The account to be created
        Account account;
    }

    /**
     * Class used for the json serializer. This represents the account on the petition
     */
    private static class Account{
        /**
         * The name of the account
         */
        String name;
        /**
         * The owner key address
         */
        String owner_key;
        /**
         * The active key address
         */
        String active_key;
        /**
         * The memo key address
         */
        String memo_key;
    }

    public static class ServiceGenerator{
        public static String TAG = "ServiceGenerator";
        public static String API_BASE_URL;
        private static HttpLoggingInterceptor logging;
        private static OkHttpClient.Builder clientBuilder;
        private static Retrofit.Builder builder;

        private static HashMap<Class<?>, Object> Services;

        public ServiceGenerator(String apiBaseUrl) {
            API_BASE_URL= apiBaseUrl;
            logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder = new OkHttpClient.Builder().addInterceptor(logging);
            builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create());
            Services = new HashMap<Class<?>, Object>();
        }

        public static <T> void setService(Class<T> klass, T thing) {
            Services.put(klass, thing);
        }

        public <T> T getService(Class<T> serviceClass) {

            T service = serviceClass.cast(Services.get(serviceClass));
            if (service == null) {
                service = createService(serviceClass);
                setService(serviceClass, service);
            }
            return service;
        }

        public static <S> S createService(Class<S> serviceClass) {

            clientBuilder.interceptors().add(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    okhttp3.Request original = chain.request();
                    okhttp3.Request.Builder requestBuilder = original.newBuilder().method(original.method(), original.body());

                    okhttp3.Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
            clientBuilder.readTimeout(5, TimeUnit.MINUTES);
            clientBuilder.connectTimeout(5, TimeUnit.MINUTES);
            OkHttpClient client = clientBuilder.build();
            Retrofit retrofit = builder.client(client).build();
            return retrofit.create(serviceClass);

        }

        public static IWebService Create() {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.interceptors().add(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    okhttp3.Request original = chain.request();

                    // Customize the request
                    okhttp3.Request request = original.newBuilder().method(original.method(), original.body()).build();

                    okhttp3.Response response = chain.proceed(request);

                    return response;
                }
            });

            OkHttpClient client = httpClient.build();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(API_BASE_URL).client(client).build();

            return retrofit.create(IWebService.class);

        }
    }

    public interface IWebService {
        @Headers({"Content-Type: application/json"})
        @POST("/api/v1/accounts")
        Call<RegisterAccountResponse> getReg(@Body Map<String, HashMap> params);
    }

    public class RegisterAccountResponse {
        public Account account;
        public Error error;

        public class Error {
            public String[] base;
        }
    }
}
