/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author aditya
 */
import java.util.ArrayList;
import java.util.Random;

public class Person implements Comparable<Person> {

    private Gene gene;
    private VirusGene virusGene;
    private String category;
    private String gender;
    private String[] genomeType = {"A1", "B1", "A2", "B2"};
    private String[] genders = {"M", "F"};
    private boolean isVaccinated;
    private boolean isNaive = true;
    private boolean isRecovered;
    private boolean isInfected;
    private boolean isAlive;
    private boolean isTested;
    private int dayOfInfection;

    public Person() {
        this.gene = new HumanGene();
        Random random = new Random();
        this.category = this.genomeType[random.nextInt(4)];
        this.gender = this.genders[random.nextInt(2)];
        isAlive = true;
    }

    public Gene getGene() {
        return gene;
    }

    public VirusGene getVirusGene() {
        return virusGene;
    }

    public String getCategory() {
        return category;
    }

    public String getGender() {
        return gender;
    }

    public String[] getGenomeType() {
        return genomeType;
    }

    public String[] getGenders() {
        return genders;
    }

    public void setGene(Gene gene) {
        this.gene = gene;
    }

    public void setVirusGene(VirusGene virusGene) {
        this.virusGene = virusGene;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setGenomeType(String[] genomeType) {
        this.genomeType = genomeType;
    }

    public void setGenders(String[] genders) {
        this.genders = genders;
    }

    public void setVaccinated(boolean vaccinated) {
        isVaccinated = vaccinated;
    }

    public void setNaive(boolean naive) {
        isNaive = naive;
    }

    public void setRecovered(boolean recovered) {
        isRecovered = recovered;
    }

    public boolean isInfected() {
        return isInfected;
    }

    public void setInfected(boolean infected) {
        isInfected = infected;
    }

    public boolean isVaccinated() {
        return isVaccinated;
    }

    public boolean isNaive() {
        return isNaive;
    }

    public boolean isRecovered() {
        return isRecovered;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public double getFitness() {
        return this.gene.getFitness();
    }

    public void vacinate() {
        this.isVaccinated = true;
        this.isNaive = false;
    }

    public void infect(VirusGene virusGene) {
        this.virusGene = virusGene;
        this.setInfected(true);
        this.setNaive(false);
    }

    public void setFitness(double newFitness) {
        this.gene.setFitness(newFitness);
    }

    @Override
    public int compareTo(Person p) {
        if (this.getFitness() > p.getFitness()) {
            return 1;
        }
        return -1;
    }

    public void fightVirus() {
        int days = 0;
        if (!isAlive) {
            return;
        }
        if (isVaccinated) {
            double virusNewFitness = 0;
            ArrayList<Double> probabilityFactor = RuntimeAttributes.fitnessTable.get(this.virusGene.getName());
            if (this.category.equals("A1")) {
                virusNewFitness = this.virusGene.getFitness() + probabilityFactor.get(8);
            } else if (this.category.equals("B1")) {
                virusNewFitness = this.virusGene.getFitness() + probabilityFactor.get(9);
            } else if (this.category.equals("A2")) {
                virusNewFitness = this.virusGene.getFitness() + probabilityFactor.get(10);
            } else if (this.category.equals("B2")) {
                virusNewFitness = this.virusGene.getFitness() + probabilityFactor.get(11);
            }
            double newFitness = this.getFitness() - virusNewFitness;
            this.setFitness(newFitness);
            if (this.getFitness() < RuntimeAttributes.humanThreshold) {
                setAlive(false);
                RuntimeAttributes.deathCount++;
                RuntimeAttributes.vaccinatedDeathCount++;
            }
        } else if (isRecovered) {
            double virusNewFitness = 0;
            ArrayList<Double> probabilityFactor = RuntimeAttributes.fitnessTable.get(this.virusGene.getName());

            if (this.category.equals("A1")) {
                virusNewFitness = this.virusGene.getFitness() + probabilityFactor.get(4);
            } else if (this.category.equals("B1")) {
                virusNewFitness = this.virusGene.getFitness() + probabilityFactor.get(5);
            } else if (this.category.equals("A2")) {
                virusNewFitness = this.virusGene.getFitness() + probabilityFactor.get(6);
            } else if (this.category.equals("B2")) {
                virusNewFitness = this.virusGene.getFitness() + probabilityFactor.get(7);
            }
            double newFitness = this.getFitness() - virusNewFitness;
            this.setFitness(newFitness);
            if (this.getFitness() < RuntimeAttributes.humanThreshold) {
                setAlive(false);
                RuntimeAttributes.deathCount++;
                RuntimeAttributes.recoveredDeathCount++;
            }
        } else if (isInfected) {
            double virusNewFitness = 0;
            ArrayList<Double> probabilityFactor = RuntimeAttributes.fitnessTable.get(this.virusGene.getName());

            if (this.category.equals("A1")) {
                virusNewFitness = this.virusGene.getFitness() * probabilityFactor.get(0);
            } else if (this.category.equals("B1")) {
                virusNewFitness = this.virusGene.getFitness() * probabilityFactor.get(1);
            } else if (this.category.equals("A2")) {
                virusNewFitness = this.virusGene.getFitness() * probabilityFactor.get(2);
            } else if (this.category.equals("B2")) {
                virusNewFitness = this.virusGene.getFitness() * probabilityFactor.get(3);
            }
            double newFitness = this.getFitness() - virusNewFitness;
            this.setFitness(newFitness);
            if (this.getFitness() < RuntimeAttributes.naiveHumanThreshold) {
                setAlive(false);
                RuntimeAttributes.deathCount++;
                RuntimeAttributes.naiveDeathCount++;
            }

        }
    }

    public boolean isTested() {
        return isTested;
    }

    public void setTested(boolean tested) {
        isTested = tested;
    }

    public int getDayOfInfection() {
        return dayOfInfection;
    }

    public void setDayOfInfection(int dayOfInfection) {
        this.dayOfInfection = dayOfInfection;
    }
}
