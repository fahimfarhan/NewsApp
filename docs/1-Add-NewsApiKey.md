# Add News Api Key
we need this key to fetch data from newsapi.org. In production, we want to keep these keys secret.
In this case, I'll be putting this in `local.properties` file as it is ignored by the git. If you keep
it in some other file, add it in the .gitignore file.

So in `local.properties` (OR your desired file), add this, may be at the end:
```
newsApiKey=123_your_top_secret_key_789
```

Now open your app gradle file, and:

```
def getNewsApiKey() {
    Properties properties = new Properties()
    properties.load(project.rootProject.file('local.properties').newDataInputStream())
    return properties.getProperty("newsApiKey");
}
```

Then add this line in default config:
```
android{
  // ... other lines
  defaultConfig{
    // ... other config
    buildConfigField "String", "NEWS_API_KEY", "\""+getNewsApiKey()+"\""        // <-- Add this line
    // ... other config
  }
}
```
Now create a file, say , `Const.kt`. We can create a constant string and initialize it with the actual
api key like this:

```
object Const {
    const val BASE_URL = "https://newsapi.org/v2/";
    const val API_KEY = BuildConfig.NEWS_API_KEY;
}
```