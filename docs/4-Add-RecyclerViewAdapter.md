# Add RecyclerView Adapter
This is just a typical recyclerView code. However, we'll be extending `PagedListAdapter<Article, RecyclerView.ViewHolder>`.
I also created some viewHolders to show 1. Stories at the top, 2. actual news items in the middle,
3. a footer at the bottom showing a loader.

Now to keep things clear, rename `position` to `uiposition`
(ie, `onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)` to `onBindViewHolder(holder: RecyclerView.ViewHolder, uiPosition: Int)` etc).

And inside, we define,
```
                dataPosition = uiPosition - OFFSET_KOUNT;
                OFFSET_KOUNT = number of header rows
```

Suppose, we have `N` items loaded from database / network. But in the ui, we have
(1 header for story) + (N data items) + (1 footer) = N+2 ui items.
So we need to make a proper mapping between our uiPosition and actual data position. So in this case,
ith data will go to (i+1)th uiPosition.

Similarly, if we had `OFFSET_KOUNT=m` headers, then `dataPosition = uiPosition - m` would be the relation.
For now, the OFFSET_KOUNT = 0. Later, I'll change it and we'll see how to implement it.