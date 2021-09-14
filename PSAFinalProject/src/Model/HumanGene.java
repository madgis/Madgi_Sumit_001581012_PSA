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
import java.util.List;
import java.util.Random;

public class HumanGene implements Gene {

    private ArrayList<String> genome;
    private double fitness;
    private String genotype;

    public HumanGene() {
        this.genome = new ArrayList<>(105);
        Random random = new Random();
        while (this.genome.size() != 105) {
            this.genome.add(this.humanNucleotides[random.nextInt(4)]);
        }
        this.genotype = this.genotypes[random.nextInt(4)];
        setFitness(random);
    }

    public void setFitness(Random random) {
        for(String s: this.genome){
            this.fitness += s.getBytes(StandardCharsets.US_ASCII)[0];
        }
        this.fitness *=2;
    }

    @Override
    public void setFitness(double value) {
        this.fitness = value;
    }

    @Override
    public double getFitness() {
        return this.fitness;
    }

    @Override
    public ArrayList<String> getGenome() {
        return this.genome;
    }

    @Override
    public String toString() {
        return "HumanGene{" +
                "genome=" + genome +
                '}';
    }
}

