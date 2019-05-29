package com.cathy.mathsearch.service;

import com.cathy.mathsearch.api.WikiApi;
import com.cathy.mathsearch.model.base.Formula;
import com.cathy.mathsearch.model.base.MathExpression;
import com.cathy.mathsearch.model.base.Request;
import com.cathy.mathsearch.model.search.Search;
import com.cathy.mathsearch.model.search.SearchResponse;
import com.cathy.mathsearch.parsers.VariableParser;
import com.cathy.mathsearch.parsers.FormulaParser;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class WikiService {

    private Retrofit retrofit;
    private WikiApi wikiApi;

    public WikiService()
    {
        retrofit =  new Retrofit.Builder()
                .baseUrl("https://ru.wikipedia.org/w/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        wikiApi = retrofit.create(WikiApi.class);
    }

    public void search(String searchText){
        FormulaParser parser = new FormulaParser();
        Request request = parser.getPattern(searchText);
        try {
            Response<SearchResponse> response = wikiApi.search(request.getRequest()).execute();
            if (response.body() != null) {
                for (final Search search: response.body().getQuery().getSearch()){
                    Thread t = new Thread(() -> {
                        parser.findMatches(request.getPattern(), search.getPageid());
                    });
                    t.start();
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR: " + e.toString());
        }
    }

    public Observable<List<List<MathExpression>>> searchByFormula(String searchText){
        FormulaParser formulaParser = new FormulaParser();
        Request request = formulaParser.getPattern(searchText);
        try {
            Response<SearchResponse> response = wikiApi.search(request.getRequest()).execute();
            if (response.body() != null) {
                return Flowable.fromIterable(response.body().getQuery().getSearch())
                        .parallel()
                        .runOn(Schedulers.computation())
                        .map(search -> {
                            FormulaParser parser = new FormulaParser();
                            return parser.findMatches(request.getPattern(), search.getPageid());
                        })
                        .sequential()
                        .toList().toObservable();
            }
        } catch (IOException e) {
            System.out.println("ERROR: " + e.toString());
        }
        return null;
    }

    public Observable<List<List<Formula>>> searchTermsFinal(String terms, RussianLuceneMorphology morphology){
        try {
            Response<SearchResponse> response = wikiApi.search("displaystyle " + terms).execute();
            if (response.body() != null) {
                return Flowable.fromIterable(response.body().getQuery().getSearch())
                        .parallel()
                        .runOn(Schedulers.computation())
                        .map(search -> {
                            VariableParser parser = new VariableParser(morphology);
                            return parser.search(search.getPageid(), terms);
                        })
                        .sequential()
                        .toList().toObservable();
            }
        } catch (IOException e) {
            System.out.println("ERROR: " + e.toString());
        }
        return null;
    }
}
