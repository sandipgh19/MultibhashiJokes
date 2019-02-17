package sandip.example.com.multibhashijokes.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import sandip.example.com.multibhashijokes.objects.CategoryResponse
import sandip.example.com.multibhashijokes.objects.ValueItem

@Dao
interface AppDao {

    @Query("SELECT * from icndb_jokes where categories LIKE '%' || :category || '%'")
    fun fetchJokesByCategory(category: List<String>): LiveData<List<ValueItem>>

    @Query("DELETE FROM icndb_jokes where categories LIKE '%' || :category || '%'")
    fun deleteJokesByCategory(category: List<String>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJokes(list: List<ValueItem?>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: CategoryResponse)

    @Query("SELECT * from icndb_jokes_category")
    fun fetchCategory(): LiveData<CategoryResponse>

    @Transaction
    fun deleteAndInsertJokes(category: List<String>, jokesList: List<ValueItem?>){
        deleteJokesByCategory(category = category)
        insertJokes(list = jokesList)
    }
}