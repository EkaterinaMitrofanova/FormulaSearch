package com.cathy.mathsearch.servlets;

import com.cathy.mathsearch.service.WikiService;
import com.google.gson.Gson;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    private RussianLuceneMorphology morphology;
    {
        try {
            morphology = new RussianLuceneMorphology();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String math = req.getParameter("math");
        String type = req.getParameter("type");
        if (type.equals("term")) {
            System.out.println(math);

            WikiService wikiService = new WikiService();
            wikiService.searchTermsFinal(math, morphology)
                    .blockingSubscribe(pairs -> {
                        String json = new Gson().toJson(pairs);
                        resp.setCharacterEncoding("UTF-8");
                        resp.setContentType("application/json");
                        resp.getWriter().write(json);
                    }, throwable -> {
                        System.out.println("ERROR: " + throwable.toString());
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, throwable.toString());
                    });
        } else {
            if (type.equals("formula")){
                WikiService wikiService = new WikiService();
                wikiService.searchByFormula(math)
                        .blockingSubscribe(pairs -> {
                            String json = new Gson().toJson(pairs);
                            resp.setCharacterEncoding("UTF-8");
                            resp.setContentType("application/json");
                            resp.getWriter().write(json);
                        }, throwable -> {
                            System.out.println("ERROR: " + throwable.toString());
                            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, throwable.toString());
                        });
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown type " + type);
            }
        }
    }
}
