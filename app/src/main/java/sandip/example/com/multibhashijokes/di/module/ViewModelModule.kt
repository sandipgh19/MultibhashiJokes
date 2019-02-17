package sandip.example.com.multibhashijokes.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sandip.example.com.multibhashijokes.di.ViewModelKey
import sandip.example.com.multibhashijokes.factory.AppModelFactory
import sandip.example.com.multibhashijokes.viewModel.CategoryListViewModel
import sandip.example.com.multibhashijokes.viewModel.JokesListViewModel

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CategoryListViewModel::class)
    abstract fun bindCategoryListViewModel(viewModel: CategoryListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JokesListViewModel::class)
    abstract fun bindJokesListViewModel(viewModel: JokesListViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: AppModelFactory): ViewModelProvider.Factory
}