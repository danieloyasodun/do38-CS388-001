package com.codepath.articlesearch

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.codepath.articlesearch.databinding.ActivityMainBinding
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.Headers
import org.json.JSONException
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService


fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

private const val TAG = "MainActivity/"
private const val SEARCH_API_KEY = BuildConfig.API_KEY
private const val ARTICLE_SEARCH_URL =
    "https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=${SEARCH_API_KEY}"

class MainActivity : AppCompatActivity() {
    private val articles = mutableListOf<DisplayArticle>()
    private lateinit var articlesRecyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var connectivityMonitor: ConnectivityMonitor
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        articleAdapter = ArticleAdapter(this, articles)
        binding.articles.layoutManager = LinearLayoutManager(this)
        binding.articles.adapter = articleAdapter

        binding.openSettingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        articlesRecyclerView = findViewById(R.id.articles)
        swipeContainer = findViewById(R.id.swipeContainer)

        sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        fetchDataBasedOnCachePreference()

        connectivityMonitor = ConnectivityMonitor(this)
        connectivityMonitor.startMonitoring()

        swipeContainer.setOnRefreshListener { refreshArticles() }

        swipeContainer.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)

        lifecycleScope.launch {
            (application as ArticleApplication).db.articleDao().getAll().collect { databaseList ->
                databaseList.map { entity ->
                    DisplayArticle(
                        entity.headline,
                        entity.articleAbstract,
                        entity.byline,
                        entity.mediaImageUrl
                    )
                }.also { mappedList ->
                    articles.clear()
                    articles.addAll(mappedList)
                    articleAdapter.notifyDataSetChanged()
                }
            }
        }

        fetchArticlesFromApi()

        articlesRecyclerView.adapter = articleAdapter
        articlesRecyclerView.layoutManager = LinearLayoutManager(this).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            articlesRecyclerView.addItemDecoration(dividerItemDecoration)
        }


        connectivityMonitor.isConnected.observe(this, Observer { isConnected ->
            if (isConnected) {
                Snackbar.make(binding.root, "Back online", Snackbar.LENGTH_SHORT).show()
                refreshArticles() // Reload data automatically when online
            } else {
                Snackbar.make(binding.root, "You are offline", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry") {
                        refreshArticles() // Option to manually retry fetching data
                    }.show()
            }
        })



        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    fetchArticles(it) // Fetch articles based on query
                    hideKeyboard(binding.searchView)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterArticles(it) } // Filter current articles
                return true
            }
        })

        fetchArticles()

    }

    private fun fetchArticlesFromApi() {
        val client = AsyncHttpClient()
        client.get(ARTICLE_SEARCH_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                Log.e(TAG, "Failed to fetch articles: $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "Successfully fetched articles: $json")
                try {
                    val parsedJson = createJson().decodeFromString(
                        SearchNewsResponse.serializer(),
                        json.jsonObject.toString()
                    )
                    parsedJson.response?.docs?.let { list ->
                        lifecycleScope.launch(IO) {
                            (application as ArticleApplication).db.articleDao().deleteAll()
                            (application as ArticleApplication).db.articleDao().insertAll(list.map {
                                ArticleEntity(
                                    headline = it.headline?.main,
                                    articleAbstract = it.abstract,
                                    byline = it.byline?.original,
                                    mediaImageUrl = it.mediaImageUrl
                                )
                            })
                        }
                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityMonitor.stopMonitoring()
    }

    private fun refreshArticles() {
        val client = AsyncHttpClient()
        client.get(ARTICLE_SEARCH_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Failed to fetch articles: $statusCode")
                swipeContainer.isRefreshing = false // Stop refresh animation on failure
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "Successfully fetched articles: $json")
                try {
                    val parsedJson = createJson().decodeFromString(
                        SearchNewsResponse.serializer(),
                        json.jsonObject.toString()
                    )
                    parsedJson.response?.docs?.let { list ->
                        lifecycleScope.launch(IO) {
                            (application as ArticleApplication).db.articleDao().deleteAll()
                            (application as ArticleApplication).db.articleDao().insertAll(list.map {
                                ArticleEntity(
                                    headline = it.headline?.main,
                                    articleAbstract = it.abstract,
                                    byline = it.byline?.original,
                                    mediaImageUrl = it.mediaImageUrl
                                )
                            })
                        }
                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                } finally {
                    swipeContainer.isRefreshing = false // Stop refresh animation on success
                }
            }
        })
    }

    private fun fetchDataBasedOnCachePreference() {
        val isCachingEnabled = sharedPreferences.getBoolean("enable_cache", false)

        fetchArticles(isCachingEnabled.toString())
    }

    private fun fetchArticles(query: String? = null) {
        // Construct the API URL based on the query
        val apiUrl = if (query.isNullOrEmpty()) {
            ARTICLE_SEARCH_URL // Default URL for fetching all articles
        } else {
            "$ARTICLE_SEARCH_URL&q=$query" // Append query for searching
        }

        val client = AsyncHttpClient()
        client.get(apiUrl, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Failed to fetch articles: $statusCode")
                swipeContainer.isRefreshing = false // Stop refresh animation on failure
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "Successfully fetched articles: $json")
                try {
                    val parsedJson = createJson().decodeFromString(
                        SearchNewsResponse.serializer(),
                        json.jsonObject.toString()
                    )
                    parsedJson.response?.docs?.let { list ->
                        lifecycleScope.launch(Dispatchers.IO) {
                            // Clear existing articles in the database
                            (application as ArticleApplication).db.articleDao().deleteAll()

                            // Insert new articles into the database
                            (application as ArticleApplication).db.articleDao().insertAll(list.map {
                                ArticleEntity(
                                    headline = it.headline?.main,
                                    articleAbstract = it.abstract,
                                    byline = it.byline?.original,
                                    mediaImageUrl = it.mediaImageUrl
                                )
                            })

                            // Retrieve caching preference
                            val isCachingEnabled = sharedPreferences.getBoolean("enable_cache", false)

                            // Cache articles if enabled
                            if (isCachingEnabled) {
                                cacheArticles(list.map { article ->
                                    DisplayArticle(
                                        headline = article.headline?.main ?: "",
                                        abstract = article.abstract ?: "",
                                        byline = article.byline?.original ?: "",
                                        mediaImageUrl = article.mediaImageUrl
                                    )
                                })
                            }
                        }
                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                } finally {
                    swipeContainer.isRefreshing = false // Stop refresh animation on success
                }
            }
        })
    }



    private fun filterArticles(query: String) {
        val filteredArticles = articles.filter {
            (it.headline?.contains(query, ignoreCase = true) == true) ||
                    (it.abstract?.contains(query, ignoreCase = true) == true)
        }
        articleAdapter.updateData(filteredArticles)
    }

    private fun cacheArticles(articles: List<DisplayArticle>) {
        lifecycleScope.launch(Dispatchers.IO) {
            // Convert DisplayArticles to ArticleEntities and insert them into the database
            (application as ArticleApplication).db.articleDao().insertAll(
                articles.map { article ->
                    ArticleEntity(
                        headline = article.headline,
                        articleAbstract = article.abstract,
                        byline = article.byline,
                        mediaImageUrl = article.mediaImageUrl
                    )
                }
            )
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}