package com.example.myapp_sherkat.interfaces;

import com.example.myapp_sherkat.classs.Present;
import com.example.myapp_sherkat.classs.ResponcePresent;
import com.example.myapp_sherkat.classs.ResponceRest;
import com.example.myapp_sherkat.classs.Rest;
import com.example.myapp_sherkat.classs.Slider;
import com.example.myapp_sherkat.classs.User;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @POST("{url}")
    Call<User> sendPost(@Path("url") String url, @Body JsonObject jsonObject);

    @GET("slider.php")
    Call<List<Slider>> getSlider();

    @POST("set_rest.php")
    Call<Rest> sendRest(@Body JsonObject jsonObject);

    @POST("{url}")
    Call<Present> sendPresent(@Path("url") String url,@Body JsonObject jsonObject);

    @GET("getPresent.php")
    Call<ResponcePresent> getPresent(@Query("iduser") int iduser, @Query("page") int page);

    @GET("getRest.php")
    Call<ResponceRest> getRest(@Query("iduser") int iduser, @Query("page") int page);


}
