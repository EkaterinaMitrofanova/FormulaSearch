package com.cathy.mathsearch.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Normalizer {

    public String removeSpecials(String text) {
        text = text
                .replaceAll("(\\\\;)|(\\\\,)|(\\\\:)|(\\\\!)|(\\\\qquad)|(\\\\quad)", " ")
                .replaceAll("[.]","")
                .replaceAll("\\s{2,}", " ")
                .replaceAll("\\\\displaystyle", " ")
                .trim()
        ;

        if (text.matches("\\{.*}")){
            text = text.substring(1, text.length()-1);
        }
        Pattern specialPattern = Pattern.compile("\\\\(operatorname).*?}|\\\\(math).*?}");
        String f = text;
        Matcher matcher = Pattern.compile(specialPattern.pattern()).matcher(text);
        while (matcher.find()){
            String opetarorname = text.substring(matcher.start(), matcher.end());
            opetarorname = opetarorname
                    .replaceAll("\\\\(operatorname).*?\\{|\\\\(math).*?\\{", "")
                    .replaceAll("}", "")
                    .replaceAll("\\\\left\\(", "(")
                    .replaceAll("\\s\\\\right\\)", ")")
            ;
            if (opetarorname.length() > 1){
                opetarorname = "\\\\" + opetarorname;
            }
            f = f.replaceFirst(specialPattern.pattern(), opetarorname);
        }
        text = f;

        specialPattern = Pattern.compile("[_^]\\s*(?!\\{).");
        f = text;
        matcher = specialPattern.matcher(text);
        while (matcher.find()) {
            String special = String.valueOf(text.charAt(matcher.end()-1));
            String symbol = String.valueOf(text.charAt(matcher.start()));
            f = f.replaceFirst(specialPattern.pattern(),  symbol + "{" + special + "}");
        }

        return f.trim();
    }

    public boolean isVariable(String text){
        return !text.contains("=");
    }
}
