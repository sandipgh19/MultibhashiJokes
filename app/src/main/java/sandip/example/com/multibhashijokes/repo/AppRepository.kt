package sandip.example.com.multibhashijokes.repo

import android.arch.lifecycle.LiveData
import sandip.example.com.multibhashijokes.data.AppDao
import sandip.example.com.multibhashijokes.objects.CategoryResponse
import sandip.example.com.multibhashijokes.objects.JokesResponse
import sandip.example.com.multibhashijokes.objects.ValueItem
import sandip.example.com.multibhashijokes.remote.WebServices
import sandip.example.com.multibhashijokes.utils.helperUtils.AppExecutors
import sandip.example.com.multibhashijokes.utils.remoteUtils.ApiResponse
import sandip.example.com.multibhashijokes.utils.remoteUtils.NetworkBoundResource
import sandip.example.com.multibhashijokes.utils.remoteUtils.Resource
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val webservice: WebServices,
    private val executor: AppExecutors,
    private val dao: AppDao
) {

    fun fetchCategory(): LiveData<Resource<CategoryResponse>> {
        return object : NetworkBoundResource<CategoryResponse, CategoryResponse>(executor) {
            override fun saveCallResult(item: CategoryResponse) {
                item.value?.let {
                    dao.insertCategory(item)
                }
            }

            override fun shouldFetch(data: CategoryResponse?) = true

            override fun loadFromDb() = dao.fetchCategory()

            override fun createCall() = webservice.getCategoryList()


        }.asLiveData()
    }

    fun fetchJokesByCategory(category: String): LiveData<Resource<List<ValueItem>>> {
        return object : NetworkBoundResource<List<ValueItem>, JokesResponse>(executor) {
            override fun saveCallResult(item: JokesResponse) {
                item.value?.let {
                    dao.deleteAndInsertJokes(category = listOf(category), jokesList = it)
                }
            }

            override fun shouldFetch(data: List<ValueItem>?) = true

            override fun loadFromDb() = dao.fetchJokesByCategory(category = listOf(category))

            override fun createCall() = webservice.getjokesByCategory(limitTo = category)


        }.asLiveData()
    }
}