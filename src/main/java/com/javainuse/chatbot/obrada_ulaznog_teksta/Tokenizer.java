package com.javainuse.chatbot.obrada_ulaznog_teksta;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Klasa za tokenizer inputa chatbota.
 */
public class Tokenizer {

    private InputStream model;

    public Tokenizer() throws FileNotFoundException {
        try{
            model = new FileInputStream("en-token.bin");
        } catch (Exception e){
            System.out.println("Exception:" + e.getMessage());
            throw e;
        }

    }

    /**
     * Razlomi rečenicu u riječi i punktuaciju koristeći tokenizer mogućnosti biblioteke Apache OpenNLP
     *
     * @param recenica
     * @return
     */
    public String[] tokeniziraj(String recenica) throws FileNotFoundException, IOException {

            // Inicijalizacija tokenizera
            TokenizerME myCategorizer = new TokenizerME(new TokenizerModel(model));

            // Tokeniziranje recenice.
            String[] tokens = myCategorizer.tokenize(recenica);

            return tokens;
    }
}
