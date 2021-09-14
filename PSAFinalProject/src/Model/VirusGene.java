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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class VirusGene implements Gene, Comparable<Gene> {

    private ArrayList<String> genome;
    private double fitness;
    private String name;



    public void setGenome(ArrayList<String> genome) {
        this.genome = genome;
    }


    public VirusGene() {
        this.genome = new ArrayList<>(105);
        Random random = new Random();
        while (this.genome.size() != 105) {
            this.genome.add(this.nucleotides[random.nextInt(this.nucleotides.length)]);
        }
        setFitness();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void mutate() {
        for (int i = 0; i < 3; i++) {
            Random random = new Random();
            Collections.swap(this.genome, random.nextInt(105), random.nextInt(105));
            Collections.swap(this.genome, random.nextInt(105), random.nextInt(105));
        }
        this.setFitness();
    }

    public void mutateAfterVaccination() {
        for (int i = 0; i < 5; i++) {
            Random random = new Random();
            Collections.swap(this.genome, random.nextInt(105), random.nextInt(105));
        }
        this.setFitness();
    }


    public void setFitness() {
        int count = 0;
        this.fitness = 0;
        for (String s : this.genome) {
            if (s.equals("ORF1a") && count < 48)
                this.fitness++;
            else if (s.equals("ORF1b") && (count >= 48 && count < 76))
                this.fitness++;
            else if (s.equals("S") && (count >= 76 && count < 90))
                this.fitness++;
            else if (s.equals("ORF3a") && (count >= 90 && count < 93))
                this.fitness++;
            else if (s.equals("E") && count == 93)
                this.fitness++;
            else if (s.equals("M") && (count >= 94 && count < 96))
                this.fitness++;
            else if (s.equals("ORF6") && count == 96)
                this.fitness++;
            else if (s.equals("ORF7a") && (count >= 97 && count < 99))
                this.fitness++;
            else if (s.equals("ORF8") && (count >= 99 && count < 101))
                this.fitness++;
            else if (s.equals("ORF10") && (count >= 101 && count < 105))
                this.fitness++;
            count++;

        }
    }



    public VirusGene crossOver(VirusGene v) {
        for (int i = 0; i < this.genome.size() / 2; i++) {
            this.genome.set(i, v.getGenome().get(i));
        }
        this.setFitness();
        return this;
    }

    public ArrayList<String> getGenome() {
        return genome;
    }

//    @Override
//    public void setFitness(double value) {
//        this.fitness = value;
//    }

    @Override
    public void setFitness(double value) {

    }

    @Override
    public double getFitness() {
        return this.fitness;
    }

    @Override
    public String toString() {
        return "VirusGene{" +
                "genome=" + genome +
                '}';
    }

    @Override
    public int compareTo(Gene o) {
        //TO-DO
        return 0;
    }
}

