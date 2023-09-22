package com.javainuse.chatbot.obrada_ulaznog_teksta;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Klasa za POS tagger inputa chatbota.
 */
public class POS_Tagger {

    private POSTaggerME posTagger;

    public POS_Tagger() throws IOException {
        try{
            InputStream model = new FileInputStream("en-pos-maxent.bin");
            posTagger = new POSTaggerME(new POSModel(model));
        } catch (Exception e){
            System.out.println("Exception:" + e.getMessage());
            throw e;
        }

    }

    /**
     * POS tagger koristi Apache OpenNLP tehnologiju kako bi pronašao Part-Of-Speech (POS) tag-ove svih tokena
     *
     *
     * @param tokeni
     * @return
     */
    public String[] tag(String[] tokeni) throws IOException {

            // Pronađi Part-Of-Speech (POS) tag-ove svih tokena
            String[] posTags = posTagger.tag(tokeni);

            return posTags;

    }
}
