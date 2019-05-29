package com.cathy.mathsearch.api;

import com.cathy.mathsearch.model.page.PageResponse;
import com.cathy.mathsearch.model.search.SearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WikiApi {

    @GET("api.php?action=query&list=search&format=json&utf8=&srlimit=100")
    Call<SearchResponse> search(@Query("srsearch") String query);

    @GET("api.php?action=parse&format=json&utf8=&prop=text")
    Call<PageResponse> getPage(@Query("pageid") int pageId);
}
