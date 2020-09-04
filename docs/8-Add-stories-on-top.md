# Add a top Row for Stories
You may want to add one or two extra rows on top. Say, facebook / instagram stories, stuff like that.
Open the `NewsFeedAdapter`, and add `val OFFSET_KOUNT = 1;`
Then on `bindViewHolder` add this at the top:
```
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, uiPosition: Int) {
        var dataPosition:Int = uiPosition - OFFSET_KOUNT;

        if(uiPosition < OFFSET_KOUNT) {                                                   //  <-- Add this block at top
            // top row                                                                    //
            var storiesViewHolder: StoriesViewHolder = holder as StoriesViewHolder;       //
            storiesViewHolder.bind(images);                                               // <-- Add this block at top
        }

        else if(0 <= dataPosition && dataPosition < super.getItemCount()) {
            var article: Article? = getItem(dataPosition);
            var articleViewHolder:ArticleViewHolder = holder as ArticleViewHolder;
            articleViewHolder.bind(article);
        }else {
          // bind the footer
        }
    }
```

And in `getItemViewType`, add :
```
    override fun getItemViewType(uiPosition: Int): Int {
        var dataPosition:Int = uiPosition - OFFSET_KOUNT;
        if(uiPosition == 0) {
            return STORY_TYPE;
        } else if(dataPosition < super.getItemCount()) {
            return ARTICLE_TYPE;
        }
        return FOOTER_TYPE;
    }

```

Finally, save and run the code. Tou should see a title `Stories` at the top.

![story_only_title.png](story_only_title.png)

Now this is too boring.
So I used newsapi to fetch the top headlines. I only took their photos. Then I added a horizontal
recyclerView to show them. Now it looks much better. Now it looks like this:

![with-stories.gif](with-stories.gif)