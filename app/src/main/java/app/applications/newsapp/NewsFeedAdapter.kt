package app.applications.newsapp

import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.applications.newsapp.model.Article
import app.applications.newsapp.viewholders.ArticleViewHolder
import app.applications.newsapp.viewholders.FooterViewHolder
import app.applications.newsapp.viewholders.StoriesViewHolder
import com.squareup.picasso.Picasso


class NewsFeedAdapter : PagedListAdapter<Article, RecyclerView.ViewHolder> {
    /** Konsts */
    companion object{  // kinda java static in kotlin style
        val TAG = NewsFeedAdapter::class.java.simpleName;
        val OFFSET_KOUNT = 1;                              // <-- We'll need this later
        val STORY_TYPE = 0;
        val ARTICLE_TYPE = 1;
        val FOOTER_TYPE = 2;
    }

    /** Variables */
    private var state: LoaderState;

    /** Konstructors */
    public constructor():super(ArticlesDiffUtilCallBack()) {  // <-- bcz it's similar to java
        this.state = LoaderState.LOADING;
    }

    /** overriden methods */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            STORY_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_stories, parent, false)
                return StoriesViewHolder(view);
            }
            ARTICLE_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_article, parent, false)
                return ArticleViewHolder(view);
            }
            else -> { // footer
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.row_footer, parent, false)
                return FooterViewHolder(view);
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, uiPosition: Int) {
        var dataPosition:Int = uiPosition - OFFSET_KOUNT;

        if(uiPosition < OFFSET_KOUNT) {
            // top row
        }else if(0 <= dataPosition && dataPosition < super.getItemCount()) {
            var article: Article? = getItem(dataPosition);
            var articleViewHolder:ArticleViewHolder = holder as ArticleViewHolder;
            articleViewHolder.bind(article);
        }else {
          // bind the footer
        }
    }

    override fun getItemViewType(uiPosition: Int): Int {
        var dataPosition:Int = uiPosition - OFFSET_KOUNT;
        if(uiPosition == 0) {
            return STORY_TYPE;
        } else if(dataPosition < super.getItemCount()) {
            return ARTICLE_TYPE;
        }
        return FOOTER_TYPE;
    }

    override fun getItemCount(): Int {
        return OFFSET_KOUNT + super.getItemCount() + isLoading();
    }

    /** private methods */
    fun isLoading():Int {
        if(state.equals(LoaderState.LOADING))
            return 1;
        return 0;
    }

    /** public apis */
    public fun setLoaderState(newState: LoaderState) {
        this.state = newState;
        notifyDataSetChanged();
    }
}