package com.cathy.mathsearch.parsers;

import com.cathy.mathsearch.model.base.MathExpression;
import com.cathy.mathsearch.model.base.Request;
import com.cathy.mathsearch.util.Constants;
import com.cathy.mathsearch.util.Normalizer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormulaParser {

    private Normalizer normalizer = new Normalizer();

    public Request getPattern(String formula){
        formula = normalizer.removeSpecials(formula);

        // replace symbols with space
        String formula2 = formula.replaceAll("[\\W_[0-9]&&[^\\\\]]", " ")
                .replaceAll("\\\\", " \\\\").trim();

        //add slash before symbols
        formula = formula.replaceAll("([\\W&&[^\\\\\\s=]])", "\\\\$1");

        String[] vars = formula2.split("\\s+");
        List<String> correctVars = new ArrayList<>();

        for (String var : vars){
            if (!var.matches("\\\\\\w+") && var.length() > 1){
                correctVars.addAll(Arrays.asList(var.split("")));
            } else {
                correctVars.add(var);
            }
        }
        Set<String> set = new LinkedHashSet<>(correctVars);

        int duplCount = 0;
        for (String s : set){
            if (s.matches("\\\\\\w+")){
                if (!Arrays.asList(Constants.vars).contains(s.toLowerCase())){  // function
                    formula = formula.replaceAll(Pattern.quote(s), "@F\\" + s +"@FE");
                }
            } else {                          // variable
                int duplicates = Collections.frequency(correctVars, s);
                String simpleSymbol = "(\\\\\\\\(?!operatorname)\\\\w+\\\\W|[\\\\w&&[^0-9]])";
                if (duplicates > 1) {
                    duplCount++;
                    formula = formula.replaceFirst(s + "(?!\\w*?@F)",  simpleSymbol );
                    formula = formula.replaceAll(s + "(?!\\w*?@F)", "\\\\" + duplCount);
                } else {
                    duplCount++;
                    formula = formula.replaceAll(s + "(?!\\w*?@F)", simpleSymbol);
                }
            }
        }
        formula = formula.replaceAll("@F\\\\", "\\\\\\\\").replaceAll("@FE", " ")
                .replaceAll(" ", "[\\\\s\\\\W]");

        System.out.println("Pattern = " + formula);

        StringBuilder request = new StringBuilder();
        for (String s: set)
            request.append(s).append(" ");
        return new Request(Pattern.compile(formula), request.toString());
    }


    public List<MathExpression> findMatches(Pattern pattern, int id){
        Document doc;
        try {
            doc = Jsoup.connect("https://ru.wikipedia.org/?curid=" + id).get();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        List<MathExpression> expressions = new ArrayList<>();
        Elements math_elements = doc.getElementsByTag("math");
        for (Element math_element : math_elements){
            String math = math_element.attr("alttext")
                    .replaceFirst("\\{\\\\displaystyle ", "")
                    .replaceAll("\\\\left\\(", "(")
                    .replaceAll("\\\\right\\)", ")")
                    ;
            math = math.substring(0, math.length()-1);

            Matcher m = Pattern.compile(".*" + pattern + ".*").matcher(math);

            if (m.matches()){
                math = math.replaceAll("(" + pattern.pattern() + ")", "\\\\color\\{orange}\\{$1}");
                MathExpression expression = new MathExpression();
                expression.setName(math);
                expression.setTopic(getNearTitle(math_element, doc));
                expression.setLink("https://ru.wikipedia.org/?curid=" + id);
                expression.setTitle(doc.getElementById("firstHeading").text());
                expressions.add(expression);
            }
        }
        return expressions;
    }

    private String getNearTitle(Element element, Document doc){
        int index = doc.getAllElements().indexOf(element);
        Elements titleElements = doc.getElementsByAttributeValue("class", "mw-headline");

        Element titleElem = null;
        for (int i = 0; i < titleElements.size(); i++){
            Element e = titleElements.get(i);
            if (doc.getAllElements().indexOf(e) > index){
                if (i == 0){
                    titleElem = doc.getElementsByTag("h1").first();
                } else {
                    titleElem = titleElements.get(i - 1);
                }
                break;
            }
        }
        if (titleElem != null){
            return titleElem.text();
        } else {
            return "";
        }
    }
}
