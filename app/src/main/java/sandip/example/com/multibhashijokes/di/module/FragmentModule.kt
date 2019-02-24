package sandip.example.com.multibhashijokes.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sandip.example.com.multibhashijokes.fragments.CategoryListFragment
import sandip.example.com.multibhashijokes.fragments.JokesListFragment

@Suppress("unused")
@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeCategoryListFragment(): CategoryListFragment

    @ContributesAndroidInjector
    abstract fun contributeJokesListFragment(): JokesListFragment



}