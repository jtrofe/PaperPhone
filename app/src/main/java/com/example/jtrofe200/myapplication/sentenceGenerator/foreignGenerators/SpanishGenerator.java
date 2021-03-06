package com.example.jtrofe200.myapplication.sentenceGenerator.foreignGenerators;


public class SpanishGenerator{
    public static String MakeSentence(String baseText, String[] returnedWords){
        String sentence = baseText;

        //*Replace question marks in base text with respective words
        for(String word:returnedWords){
            sentence = sentence.replaceFirst(" \\?", " " + word);
        }

        while(sentence.contains("@")){
            sentence = MakeAgreements(sentence);
        }
        sentence = sentence.replaceAll("m\\|", "").replaceAll("f\\|", "");

        //*Capitalize first letter
        String firstLetter = sentence.substring(0, 1).toUpperCase();
        sentence = firstLetter + sentence.substring(1);

        return sentence.trim();
    }

    private static String MakeAgreements(String sentence){
        //*Finds the noun that is being pointed to after a @ symbol
        //*  replaces it with a/o/as/os depending on the gender specified
        //*  by the m/f| before the noun
        int articleIndex = sentence.indexOf("@");
        String direction = sentence.substring(articleIndex+1, articleIndex+2);
        String restOfSentence;
        int beginningOfVariable;
        int beginningOfNoun;
        int endOfNoun;
        String variable;
        String noun;
        String nounGender;
        String nounLastLetter;
        String baseVariable;

        if(direction.equals("<")){
            restOfSentence = sentence.substring(0, articleIndex);

            beginningOfVariable = restOfSentence.lastIndexOf(" ");

            String rest2 = restOfSentence.substring(0, beginningOfVariable-2);
            beginningOfNoun = rest2.lastIndexOf("|") - 1;
            if(beginningOfNoun == -1) beginningOfNoun = 0;
            endOfNoun = restOfSentence.indexOf(" ", beginningOfNoun);
            if(endOfNoun == -1) endOfNoun = restOfSentence.length();


            baseVariable = restOfSentence.substring(beginningOfVariable);
            noun = restOfSentence.substring(beginningOfNoun, endOfNoun);
        }else{
            restOfSentence = sentence.substring(sentence.indexOf("|")-1);
            beginningOfVariable = sentence.lastIndexOf(" ", articleIndex);
            if(beginningOfVariable == -1) beginningOfVariable = 0;
            baseVariable = sentence.substring(beginningOfVariable, articleIndex);
            endOfNoun = restOfSentence.indexOf(" ");
            if(endOfNoun == -1) endOfNoun = restOfSentence.length();

            noun = restOfSentence.substring(0, endOfNoun);
        }


        nounLastLetter = noun.substring(noun.length()-1);

        nounGender = noun.substring(0, 1);

        variable = baseVariable;
        if(nounGender.equals("f")){
            variable += "a";
        }else{
            variable += "o";
        }
        if(variable.toLowerCase().equals("uno")) variable = variable.substring(0, 2);
        if(variable.toLowerCase().equals("lo")) variable = "el";

        if(nounLastLetter.equals("s")) variable += "s";
        if(variable.toLowerCase().equals("els")) variable = "los";

        if(variable.toLowerCase().equals("uns")) variable = "unos";
        if(variable.toLowerCase().equals("uno")) variable = "un";


        sentence = sentence.replace(baseVariable + "@" + direction, variable);

        return sentence;
    }
}