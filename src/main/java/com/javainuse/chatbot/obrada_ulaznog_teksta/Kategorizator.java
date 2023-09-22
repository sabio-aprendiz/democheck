package com.javainuse.chatbot.obrada_ulaznog_teksta;

import opennlp.tools.doccat.*;
import opennlp.tools.util.*;
import opennlp.tools.util.model.ModelUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Klasa za kategorizator inputa chatbota.
 */
public class Kategorizator {

    private DoccatModel model;

    public DoccatModel getModel() {
        return model;
    }

    public void setModel(DoccatModel model) {
        this.model = model;
    }

    /**
     * Konstruktor za kategorizator. Ujedno trenira kategorizator.
     *
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Kategorizator() throws FileNotFoundException, IOException {

        // faq-kategorije.txt je ručno proizvedni skup podataka za treniranje sa označenim klasama
        InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(new File("faq-kategorije.txt"));
        ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
        ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

        //stvaramo novi objekt klase doccat factory (document-categorizator factory)
        DoccatFactory factory = new DoccatFactory(new FeatureGenerator[] { new BagOfWordsFeatureGenerator() });
        //stvaramo i definiramo parametre za treniranje modela kao default parametre
        TrainingParameters parametres = ModelUtil.createDefaultTrainingParameters();
        parametres.put(TrainingParameters.CUTOFF_PARAM, 0);

        // Treniranje modela sa klasifikacijama iz teksta
        model = DocumentCategorizerME.train("en", sampleStream, parametres, factory);
    }

    /**
     * Detektiraj najvjerojatniju kategoriju za token.
     *
     * @param finalTokens
     * @return
     */
    public String vratiKategoriju(String[] finalTokens) throws IOException {

        // Definiraj novi kategorizator iz modela
        DocumentCategorizerME openNLPKategorizator = new DocumentCategorizerME(model);

        // Nađi najvjerojatniju kategoriju
        double[] vjerojatnostiPoKategorijama = openNLPKategorizator.categorize(finalTokens);
        String kategorija = openNLPKategorizator.getBestCategory(vjerojatnostiPoKategorijama);

        return kategorija;
    }
}
