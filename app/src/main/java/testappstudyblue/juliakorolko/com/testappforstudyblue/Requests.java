package testappstudyblue.juliakorolko.com.testappforstudyblue;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by juliakorolko on 3/17/17.
 */

public interface Requests {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/users/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("ocramius/repos")
    Call<ResponseBody> getUserData();
}
