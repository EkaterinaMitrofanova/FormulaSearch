package com.cathy.mathsearch.parsers;

import com.cathy.mathsearch.model.base.Formula;
import com.cathy.mathsearch.model.base.MathExpression;
import com.cathy.mathsearch.model.base.Term;
import com.cathy.mathsearch.util.Normalizer;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableParser {

    private Normalizer normalizer = new Normalizer();
    private RussianLuceneMorphology morphology;

    private String title, link;
    private List<MathExpression> allMathExpressions = new ArrayList<>();
    private Elements titleElements;
    private Map<String, Formula> mapMaths = new ConcurrentHashMap<>();

    public VariableParser(RussianLuceneMorphology morphology) {
        this.morphology = morphology;
    }


    public List<Formula> search(Integer id, String request){
        Document doc;
        try {
            doc = Jsoup.connect("https://ru.wikipedia.org/?curid=" + id).get();
        } catch (java.net.UnknownHostException e){
            System.out.println("Check Internet connection!");
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (doc == null) return new ArrayList<>();

        Element element = doc.getElementById("firstHeading");
        if (element == null){
            return new ArrayList<>();
        }

        link = "https://ru.wikipedia.org/?curid=" + id;

        try {
            doc.getElementById("bodyContent").children().first().remove();
        } catch (NullPointerException ignored) {
        }


        Elements tables = doc.getElementsByTag("table");
        tables.addAll(doc.getElementsByClass("thumb tright"));
        tables.addAll(doc.getElementsByClass("mw-editsection"));
        if (!tables.isEmpty()) {
            tables.remove();
        }

        Elements mrows = doc.select("math > semantics > mrow");
        if (!mrows.isEmpty()){
            mrows.remove();
        }

        String text = prepareMaths(doc);

        return find(text, request.split("\\s*,\\s*"));
    }

    private String prepareMaths(Document doc){
        Elements maths = doc.select("dl math annotation");
        for (Element math: maths){
            math.addClass("its-math");
            String text = math.text();
            text = normalizer.removeSpecials(text);
            if (normalizer.isVariable(text)){
                math.text("@TS" + text + "@TE");
            } else {
                math.text("@MS" + text + "@ME(" + getTopic(math, doc) + ")");
            }
        }

        Elements vars = doc.getElementsByTag("annotation").not(".its-math");
        for (Element var: vars){
            String text = normalizer.removeSpecials(var.text());
            if (normalizer.isVariable(text)){
                var.text("@TS" + text + "@TE");
            } else {
                var.text("@MS" + text + "@ME(" + getTopic(var, doc) + ")");
            }
        }

        Elements temps = doc.getElementsByClass("math-template");
        for (Element temp: temps){
            String text = normalizer.removeSpecials(temp.text());
            if (normalizer.isVariable(text)) {
                temp.text("@TS" + temp.text().trim() + "@TE");
            } else {
                temp.text("@MS" + temp.text().trim() + "@ME(" + getTopic(temp, doc) + ")");
            }
        }

        String docText = doc.text();
        String finalDocText = doc.text();
        Matcher matcher = Pattern.compile("@MS(.+?)@ME\\((.+?)\\)").matcher(finalDocText);
        while (matcher.find()){
            String math = matcher.group(1);
            String topic = matcher.group(2);
            MathExpression expression = new MathExpression(math, matcher.start(), topic);
            allMathExpressions.add(expression);
            docText = docText.replaceFirst(matcher.pattern().pattern(), ".");
        }
        return docText;
    }

    private String getTopic(Element element, Document doc){
        int index = doc.getAllElements().indexOf(element);
        if (titleElements == null){
            titleElements = doc.getElementsByAttributeValue("class", "mw-headline");
        }
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

    private List<Formula> find(String docText, String[] words){
        String[] docArray = docText
                .replaceAll("([[\\W]&&[^а-яА-Я\\s]])", " $1 ")
                .split("\\s+");
        List<String> lemmaArray = new ArrayList<>(docArray.length);
        for (int i = 0; i < docArray.length; i++){
            String s = docArray[i].trim();
            if (s.matches("[а-яА-Я]+")) {
                try {
                    s = morphology.getNormalForms(s).get(0);
                }catch (Exception ignored){

                }
            }
            lemmaArray.add(i, s.toLowerCase());
        }

        for (String word: words) {
            StringBuilder builder = new StringBuilder();
            Set<String> sequenceSet = new LinkedHashSet<>();

            word = word.trim();

            List<String> wordsLemma = Arrays.asList(word.toLowerCase().split(" "));
            for (int i = 0; i < wordsLemma.size(); i++){
                String s = wordsLemma.get(i);
                try {
                    s = morphology.getNormalForms(s).get(0);
                }catch (Exception ignored){

                }
                wordsLemma.set(i, s);
            }
            int prevIndex = -1;
            for (int i = 0; i < lemmaArray.size(); i++){
                int index = wordsLemma.indexOf(lemmaArray.get(i));
                if (index == -1) continue;
                if (index == 0){
                    prevIndex = 0;
                    if (builder.toString().isEmpty()){
                        builder.append(docArray[i]).append(" ");
                        continue;
                    }
                    if (builder.toString().trim().split("\\s").length == wordsLemma.size()){
                        sequenceSet.add(builder.toString().trim());
                        builder = new StringBuilder();
                        builder.append((docArray[i])).append(" ");
                    }
                } else {
                    if (index - prevIndex == 1){
                        builder.append(docArray[i]).append(" ");
                        prevIndex = index;
                    } else {
                        if (builder.toString().trim().split("\\s").length == wordsLemma.size()){
                            sequenceSet.add(builder.toString().trim());
                        }
                        builder = new StringBuilder();
                    }
                }

            }

            List<Term> terms = new ArrayList<>();
            for (String sequence : sequenceSet){
                Matcher matcher = Pattern.compile(sequence + "[\\W_&&[^а-я-А-Я]]").matcher(docText);
                while (matcher.find()){
                    int termIndex = matcher.start();
                    String sentence;
                    sentence = docText.substring(termIndex, matcher.end()-1 + 50);
                    if (sentence.contains(".")){
                        sentence = sequence.replaceAll("\\..*", "");
                    }

                    int startTerm = termIndex-1;
                    StringBuilder stringBuilder = new StringBuilder();
                    while (startTerm > -1 && !String.valueOf(docText.charAt(startTerm)).matches("[.;:]")){
                        stringBuilder.append(docText.charAt(startTerm));
                        startTerm--;
                    }
                    sentence = stringBuilder.reverse().toString() + sentence;

                    if (!sentence.contains("@TS")) continue;

                    int sequenceIndex = sentence.indexOf(sequence);
                    int symbolIndex = -1;
                    int symbolAndTermRange = 300;
                    String symbol = "";
                    Matcher symbolMatcher = Pattern.compile("@TS.+?@TE").matcher(sentence);
                    while (symbolMatcher.find()){
                        String s = sentence.substring(symbolMatcher.start(), symbolMatcher.end());
                        Pattern p = Pattern.compile(s.replaceAll("([\\W&&[^\\s=]])", "\\\\$1") + "\\s*((-)|(—))\\s*" + sequence);
                        int range = Math.abs(symbolMatcher.end() - sequenceIndex);
                        if (p.matcher(sentence).find()){
                            symbol = s;
                            symbolAndTermRange = range;
                            symbolIndex = symbolMatcher.start();
                            break;
                        }
                        if (range < symbolAndTermRange) {
                            symbol = s;
                            symbolAndTermRange = range;
                            symbolIndex = symbolMatcher.start();
                        }
                    }
                    if (symbol.isEmpty()){
                        continue;
                    }
                    Term term = new Term();
                    term.setName(sequence);
                    term.setRange(termIndex);
                    symbol = symbol.replaceAll("(@TS)|(@TE)", "");
                    term.setSymbol(symbol);
                    term.setText("…" + sentence
                            .replaceAll("(@TS)(" + symbol.replaceAll("([\\W&&[^\\s=]])", "\\\\$1") + ")(@TE)",
                                    "@TS\\\\color\\{orange}\\{$2}@TE")
                            .replaceAll("(@TS)", "\\\\(")
                            .replaceAll("(@TE)", "\\\\)")
                            .replaceAll("(@MS)", "\\\\(")
                            .replaceAll("(@ME)", "\\\\)")
                    + "…");
                    terms.add(term);
                }
            }
            findMaths(terms);
        }

        Iterator<String> it1 = mapMaths.keySet().iterator();
        while(it1.hasNext()){
            String key = it1.next();
            if(mapMaths.get(key).getTerms().size() != words.length){
                mapMaths.remove(key);
            }
        }

        return new ArrayList<>(mapMaths.values());
    }


    private void findMaths(List<Term> terms){
        for (MathExpression expression: allMathExpressions){
            Term termResult = null;
            int range = 1000;
            for (Term term: terms){
                if (term.getSymbol().length() == 1){
                    if (!expression.getName().replaceAll("\\\\\\w+?[\\W_]", "").contains(term.getSymbol())){
                        continue;
                    }
                }
                boolean stop = false;
                if (term.getSymbol().contains(",")){
                    String[] s = term.getSymbol().split("\\s*,\\s*");
                    for (String ss: s){
                        if (!expression.getName().contains(ss)){
                            stop = true;
                            break;
                        }
                    }
                    if (stop){
                        continue;
                    }
                } else {
                    if (!expression.getName().contains(term.getSymbol())) {
                        continue;
                    }
                }
                int r = Math.abs(expression.getRange() - term.getRange());
                if (r < range){
                    range = r;
                    termResult = term;
                }
            }
            if (termResult == null) continue;
            if (mapMaths.containsKey(expression.getName())){
                List<Term> termList = mapMaths.get(expression.getName()).getTerms();
                termList.add(termResult);
            } else {
                List<Term> termList = new ArrayList<>();
                termList.add(termResult);
                mapMaths.put(expression.getName(), new Formula(expression.getName(), title, link, termList, expression.getTopic()));
            }
        }
    }
}
