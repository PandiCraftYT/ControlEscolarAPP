package com.example.controlescolar;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("api/android/authenticate")
    Call<ApiResponse> authenticate(@Body JsonObject body);

    public class ApiResponse {
        private boolean success;
        private String message;
        private Data data;

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Data getData() {
            return data;
        }

        public class Data {
            // Puedes agregar más campos según lo que devuelva tu API
        }
    }
}
