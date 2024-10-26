package com.codepath.articlesearch

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

const val ARTICLE_EXTRA = "ARTICLE_EXTRA"
private const val TAG = "ArticleAdapter"

class ArticleAdapter(private val context: Context, private val displayArticles: MutableList<DisplayArticle>) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val articles = displayArticles[position]
        holder.bind(articles)
    }

    override fun getItemCount() = displayArticles.size

    fun updateData(newArticles: List<DisplayArticle>) {
        displayArticles.clear() // Clear current articles
        displayArticles.addAll(newArticles) // Add new articles
        notifyDataSetChanged() // Notify the adapter of the data change
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val mediaImageView = itemView.findViewById<ImageView>(R.id.mediaImage)
        private val titleTextView = itemView.findViewById<TextView>(R.id.mediaTitle)
        private val abstractTextView = itemView.findViewById<TextView>(R.id.mediaAbstract)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(articles: DisplayArticle) {
            titleTextView.text = articles.headline
            abstractTextView.text = articles.abstract

            Glide.with(context)
                .load(articles.mediaImageUrl)
                .placeholder(R.drawable.landscape_placeholder)
                .into(mediaImageView)
        }

        override fun onClick(v: View?) {
            // Get selected article
            val article = displayArticles[absoluteAdapterPosition]

            // Navigate to Details screen and pass selected article
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(ARTICLE_EXTRA, article)
            context.startActivity(intent)
        }
    }

}
