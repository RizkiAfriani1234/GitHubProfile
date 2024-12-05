package id.ac.polbeng.rizkiafriani.githubprofile.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.room.util.query
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.ac.polbeng.rizkiafriani.githubprofile.R
import id.ac.polbeng.rizkiafriani.githubprofile.databinding.ActivityMainBinding
import id.ac.polbeng.rizkiafriani.githubprofile.helpers.Config
import id.ac.polbeng.rizkiafriani.githubprofile.models.GithubUser
import id.ac.polbeng.rizkiafriani.githubprofile.services.GithubUserService
import id.ac.polbeng.rizkiafriani.githubprofile.services.ServiceBuilder
import id.ac.polbeng.rizkiafriani.githubprofile.viewmodels.MainViewModel
import retrofit2.Call

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)
        mainViewModel.githubUser.observe(this) { user ->
            setUserData(user)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }


        binding.btnSearchUserLogin.setOnClickListener {
            val userLogin = binding.etSearchUserLogin.text.toString()
            if (userLogin.isNotEmpty()) {
                searchUser(userLogin)
                mainViewModel.searchUser(userLogin) // Kirim userLogin sebagai parameter.
            }

        }
        searchUser(Config.DEFAULT_USER_LOGIN)
    }
    private fun searchUser(query: String){
        showLoading(true)
        Log.d(TAG, "getDataUserProfileFromAPI: start...")
        val githubUserService: GithubUserService = ServiceBuilder.buildService(GithubUserService::class.java)
        val requestCall: Call<GithubUser> = githubUserService.loginUser(query)
        requestCall.enqueue(object : retrofit2.Callback<GithubUser> {
            override fun onResponse(call: Call<GithubUser>, response: retrofit2.Response<GithubUser>) {
                showLoading(false)
                if(response.isSuccessful){
                    val result = response.body()
                    if (result != null) {
                        setUserData(result)
                    }
                    Log.d(TAG, "getDataUserFromAPI: onResponse finish...")
                }else{
                    binding.tvUser.text = "User Not Found"
                    Glide.with(applicationContext)
                        .load(R.drawable.ic_baseline_broken_image_24)
                        .into(binding.imgUser)
                    Log.d(TAG, "getDataUserFromAPI: onResponse failed...")
                }
            }
            override fun onFailure(call: Call<GithubUser>, t: Throwable) {
                showLoading(false)
                Log.d(TAG, "getDataUserFromAPI: onFailure ${t.message}...")
            }
        })
    }
    private fun setUserData(githubUser: GithubUser) {
        binding.tvUser.text = githubUser.toString()
        Glide.with(applicationContext)
            .load(githubUser.avatarUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .error(R.drawable.ic_baseline_broken_image_24))
            .into(binding.imgUser)
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
