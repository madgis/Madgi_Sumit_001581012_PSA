/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author chinmay
 */
import java.util.ArrayList;

public interface Gene {

    String[] nucleotides = {
            "ORF1a","ORF1a","ORF1a","ORF1a","ORF1a","ORF1a","ORF1a","ORF1a","ORF1a","ORF1a",
            "ORF1b","ORF1b","ORF1b","ORF1b","ORF1b","ORF1b",
            "S","S","S","S","S",
            "ORF3a",
            "E",
            "M","M",
            "ORF6",
            "ORF7a","ORF7a",
            "ORF8","ORF8",
            "ORF10","ORF10","ORF10"
    };
    String[] humanNucleotides = {"A", "T", "G", "C"};
    String[] genotypes = {"A1", "B1", "A2", "B2"};
    void setFitness(double value);
    double getFitness();
    ArrayList<String> getGenome();

}
    
