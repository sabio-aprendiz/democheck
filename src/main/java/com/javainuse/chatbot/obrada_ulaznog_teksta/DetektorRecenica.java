package com.javainuse.chatbot.obrada_ulaznog_teksta;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Klasa za detektor rečenica u tekstu inputa chatbota.
 */
public class DetektorRecenica {

    private InputStream model;

    public DetektorRecenica() throws FileNotFoundException {
        try{
            model = new FileInputStream("en-sent.bin");
        } catch (Exception e){
            System.out.println("Exception:" + e.getMessage());
            throw e;
        }

    }
    /**
     * Razlomi input tekst u rečenice koristeći mogućnosti za detekciju rečenica biblioteke Apache OpenNLP
     *
     *
     * @param tekst
     * @return
     */
    public String[] razlomiTesktNaRecenice(String tekst) throws FileNotFoundException, IOException {

            SentenceDetectorME sentenceDetector = new SentenceDetectorME(new SentenceModel(model));
            String[] recenice = sentenceDetector.sentDetect(tekst);

            return recenice;
    }
}
