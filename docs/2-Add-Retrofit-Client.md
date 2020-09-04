# Add Retrofit Client
We'll be using the retrofit library for our networking related stuffs. I have previously worked with
news api on other tutorials. So to save up some time, I am just copying and paste those codes from
other projects.
Basically, I have created `RetrofitClient.java` in singleton pattern. We'll be keeping our apis in
`APIService.java` file.

Depending on the api response, I have created the model, `NewsApiResponse.java`, `Article.java` and
`Sources.java`.