package com.javainuse.chatbot.obrada_ulaznog_teksta;

import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Klasa za lematizator inputa chatbota.
 */
public class Lematizator {

    private LemmatizerME lematizator;

    public Lematizator() throws IOException {
        try{
            InputStream model = new FileInputStream("en-lemmatizer.bin");
            lematizator = new LemmatizerME(new LemmatizerModel(model));
        } catch (Exception e){
            System.out.println("Exception:" + e.getMessage());
            throw e;
        }

    }

    /**
     * Nađi korijen tokena koristeći tehnologije lematizacije biblioteke Apache OpenNLP
     *
     *
     * @param tokeni
     * @param posTags
     * @return
     */
    public String[] lematiziraj(String[] tokeni, String[] posTags){

            String[] lemmaTokeni = lematizator.lemmatize(tokeni, posTags);

            return lemmaTokeni;
    }
}
