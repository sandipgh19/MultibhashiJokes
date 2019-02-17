package sandip.example.com.multibhashijokes.remote

import android.arch.lifecycle.LiveData
import retrofit2.http.GET
import retrofit2.http.Query
import sandip.example.com.multibhashijokes.objects.CategoryResponse
import sandip.example.com.multibhashijokes.objects.JokesResponse
import sandip.example.com.multibhashijokes.utils.remoteUtils.ApiResponse

interface WebServices {

    @GET("categories")
    fun getCategoryList(): LiveData<ApiResponse<CategoryResponse>>

    @GET("jokes/random/10")
    fun getjokesByCategory(@Query("limitTo") limitTo: String): LiveData<ApiResponse<JokesResponse>>
}