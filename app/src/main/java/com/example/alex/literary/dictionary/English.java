package com.example.alex.literary.dictionary;

import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;

/**
 * Created by Alex on 7/29/2016.
 */
public class English {


    // BUG DOESN'T WORK FIRST TIME YOU RUN IT

    public static String processDefinition(String inputWord) {

        String formatedString;

        String path = "src" + File.separator + "main" + File.separator + "dict";

        URL url = null;

        StringBuffer fpath = new StringBuffer(400);
        fpath.append(Environment.getExternalStorageDirectory().toString());

        System.out.println(fpath);

        File f = new File(fpath.toString());
        ArrayList<ArrayList<String>> definitions = new ArrayList<>();

        try {
            System.out.println("Am I here?");
            url = new URL("file", null, path);
            System.out.println("Am I here? 2");
            IDictionary dict = new Dictionary(f);
            System.out.println("Am I here? 3");
            dict.open();
            System.out.println("Am I here? 4");


            WordnetStemmer stem = new WordnetStemmer(dict);
            System.out.println("test " + stem.findStems(inputWord, null));

            List<String> answer = stem.findStems(inputWord, null);


            if (answer.isEmpty()) {
                formatedString = "Sorry, we cannot find the word you're looking for!";
                return formatedString;
            } else {
                try {

                    for (int i = 0; i < answer.size(); i++) {

                        System.out.println("_______________");
                        System.out.println(answer.get(i));

                        boolean verb = getPartsOfSpeech(dict, answer.get(i)).contains(POS.VERB);
                        boolean noun = getPartsOfSpeech(dict, answer.get(i)).contains(POS.NOUN);
                        boolean adjective = getPartsOfSpeech(dict, answer.get(i)).contains(POS.ADJECTIVE);
                        boolean adverb = getPartsOfSpeech(dict, answer.get(i)).contains(POS.ADVERB);

                        System.out.println(verb + " // " + noun + " // " + adjective + " // " +
                                adverb);


                        if (verb == true) {
                            IIndexWord idxWord = dict.getIndexWord(answer.get(i), POS.VERB);
                            System.out.println("Verb:\n");
                            definitions.add(printDefinition(dict, idxWord, "Verb", answer.get(i)));
                        }

                        if (noun == true) {
                            IIndexWord idxWord = dict.getIndexWord(answer.get(i), POS.NOUN);
                            System.out.println("Noun:\n");
                            definitions.add(printDefinition(dict, idxWord, "Noun", answer.get(i)));
                        }

                        if (adjective == true) {
                            IIndexWord idxWord = dict.getIndexWord(answer.get(i), POS.ADJECTIVE);
                            System.out.println("Adjective:\n");
                            definitions.add(printDefinition(dict, idxWord, "Adjective", answer.get(i)));
                        }

                        if (adverb == true) {
                            IIndexWord idxWord = dict.getIndexWord(answer.get(i), POS.ADVERB);
                            System.out.println("Adverb\n");
                            definitions.add(printDefinition(dict, idxWord, "Adverb", answer.get(i)));

                        }


                    }

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("HELLO I AM HERE!!!!!");
                }
            }
        } catch (MalformedURLException e) {
            System.out.println("Completed 2!");
        } catch (IOException e2) {
            e2.printStackTrace();
//            dfnDisplay.setText("woh");
            System.out.println("Completed!");
        }


        formatedString = definitions.toString()
                .replace(",", "\n")//remove the commas
                .replace("[", "")  //remove the right bracket
                .replace("]", "\n")  //remove the left bracket
                .trim();

        return formatedString;

    }

    private static Collection getPartsOfSpeech(IDictionary dict, String word) {
        ArrayList<POS> parts = new ArrayList<POS>();
        WordnetStemmer stemmer = new WordnetStemmer(dict);
        // Check every part of speech.
        for (POS pos : POS.values()) {
            // Check every stem, because WordNet doesn't have every surface
            // form in its database.
            for (String stem : stemmer.findStems(word, pos)) {
                IIndexWord iw = dict.getIndexWord(stem, pos);
                if (iw != null) {
                    parts.add(pos);
                }
            }
        }
        return parts;
    }

    private static ArrayList<String> printDefinition(IDictionary dict, IIndexWord idxWord, String POS, String wordName) {

        WordnetStemmer stemmer = new WordnetStemmer(dict);
        ArrayList<String> definitions = new ArrayList<String>();

        if (idxWord != null) {
            definitions.add(POS + " (" + wordName + ")\n");
        }


        for (int i = 0; i >= 0; i++) {

            String hello = "";
            Log.d(hello, "I'm here 2");

            try {
                if (idxWord != null) {

                    IWordID wordID = idxWord.getWordIDs().get(i);
                    IWord word = dict.getWord(wordID);

                    definitions.add("(" + (i + 1) + ") " + word.getSynset().getGloss() + "");
//                    dfnDisplay.setText("(" + (i + 1) + ") " + word.getSynset().getGloss() + "\n");
                    System.out.println("(" + (i + 1) + ") " + word.getSynset().getGloss());
                    System.out.println("");
                } else {
                    break;
                }
            } catch (NullPointerException e) {
//                dfnDisplay.setText("Sorry, we cannot find the word you're looking for!" + "\n");
                break;
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                break;
            } catch (IndexOutOfBoundsException e) {
                System.out.println("I'm in this Index");
                e.printStackTrace();
                break;
            } catch (Exception e) {
                break;
            }
        }

        return definitions;
    }



}
