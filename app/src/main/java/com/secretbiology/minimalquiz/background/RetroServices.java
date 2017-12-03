package com.secretbiology.minimalquiz.background;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Rohit Suratekar on 29-11-17 for Quiz.
 * All code is released under MIT License.
 */

interface RetroServices {

    //Get Currency Exchange rates
    @GET("api.php")
    Call<ResponseData> getLatestExchangeRates(@Query("amount") int amount,
                                              @Query("category") int category);
}
