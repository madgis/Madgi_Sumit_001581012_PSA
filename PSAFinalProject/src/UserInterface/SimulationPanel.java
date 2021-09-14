/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Model.CrossoverAndMutation;
import Model.LineGraph;
import Model.Person;
import Model.Population;
import Model.RuntimeAttributes;
import Model.VirusGene;
import java.awt.CardLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.ini4j.Wini;

/**
 *
 * @author aditya
 */
public class SimulationPanel extends javax.swing.JPanel {

    private JPanel body;
    private int timer;
    private boolean stopSim;
    private int numberOfDays = 0;
    private Population population;
    private LineGraph deathDataset ;
    private LineGraph infectedDataset;
    private LineGraph recoveredDataset;
    private boolean startVaccinationFlag;

    /**
     * Creates new form SimulationPanel
     */
    public SimulationPanel(JPanel container) {
        this.body = container;
        deathDataset= new LineGraph("Death");
        infectedDataset= new LineGraph("Infected");
        recoveredDataset= new LineGraph("Recovered");
        initComponents();
        this.starter();
    }

    public void startSimulation() {

        try {
            // Setting the population Size to 1000.
            Wini ini = new Wini(new File("src/UserInterface/config.ini"));
            this.population = new Population(ini.get("initial_values","population",int.class));
            RuntimeAttributes.k_factor=ini.get("initial_values","k_factor",int.class);
        } catch (IOException ex) {
            Logger.getLogger(SimulationPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        // Create a new Virus: First Variant.
        int variantCount = 1;
        VirusGene initialVariant = new VirusGene();
        String variantName = "V" + variantCount;
        initialVariant.setName(variantName);
        double latestVariantFitness = initialVariant.getFitness();

        // Add the initial Variant with probability values based on different Genotypes in the fitnessTable.
        RuntimeAttributes.fitnessTable.put(initialVariant.getName(), new ArrayList<>(Arrays.asList(2.0, 1.0, 5.0, 12.0, 1.5, 0.5, 2.5, 6.0, 1.75, 0.75, 3.75, 9.0)));
        // Add the initial Variant with its respective Fitness Value in the VirusVariantLog HashMap
        RuntimeAttributes.virusvariantLog.put(initialVariant.getName(), initialVariant.getFitness());

        // Infect one person in random from the population.
        Random populationRandom = new Random();
        Person p = population.getPersonList().get(populationRandom.nextInt(1000));
        p.infect(initialVariant);
        p.setDayOfInfection(1);

        p.fightVirus();
        
        Person p1 = population.getPersonList().get(populationRandom.nextInt(1000));
        p1.infect(initialVariant);
        p1.setDayOfInfection(1);

        p1.fightVirus();

        // loop to infect all the population
        this.startVaccinationBtn.setEnabled(false);
        this.timer = 500;
        this.oneX.setEnabled(false);
        this.reset.setEnabled(false);
        
        
        int previousDeaths=0;
        int previousInfections=0;
        int previousRecovered=0;
        while (numberOfDays < 730) {
            
            if (this.stopSim) {
                break;
            }
            if (numberOfDays == 729) {
                this.stopSimulationBtn.setEnabled(false);
                this.reset.setEnabled(true);
            }
            if (numberOfDays == 365) {
                this.startVaccinationBtn.setEnabled(true);
            }
            numberOfDays++;
            this.dayCounter.setText(String.valueOf(numberOfDays));
            try {
                Thread.sleep(timer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int testsPerDay = 1;
            int vaccinationsPerDay = 1;

            // For all the host who are infected, virus will mutate in the host cells and host will fight virus.
            for (Person per : population.getPersonList()) {
                if (per.isInfected()) {
                    per.getVirusGene().mutate();
                    per.fightVirus();
                }
                // If host is able to survive for 28 days, then the host is marked as recovered and as antibodies develop inside his body his increase the fitness by 2000
                if (per.isInfected() && (numberOfDays - per.getDayOfInfection() == 28) && per.isAlive()) {
                    per.setRecovered(true);
                    RuntimeAttributes.recoveredCount++;
                    per.setFitness(per.getFitness() + 2000);
                }
            }
            // After one year vaccinate people on random.
            if (this.startVaccinationFlag) {
                while (vaccinationsPerDay < 5) {
                    Person a = population.getPersonList().get(populationRandom.nextInt(1000));
                    Person b = population.getPersonList().get(populationRandom.nextInt(1000));
                    // If A is not vaccinated then increase his fitness and set Vaccinated Flag to be true
                    if (!a.isVaccinated()) {
                        a.setVaccinated(true);
                        a.setFitness(a.getFitness() + 2000);
                    }
                    // If B is not vaccinated then increase his fitness and set Vaccinated Flag to be true
                    if (!b.isVaccinated()) {
                        b.setVaccinated(true);
                        b.setFitness(b.getFitness() + 2000);
                    }
                    vaccinationsPerDay++;
                }
            }

            while (testsPerDay < 10) {
                testsPerDay++;
                // Get two person in random
                Person a = population.getPersonList().get(populationRandom.nextInt(1000));
                Person b = population.getPersonList().get(populationRandom.nextInt(1000));

                boolean crossOverFlag = false;
                // If A and B are infected and both come in contact then perform Crossover of virus from two different Parents.
                if (a.isInfected() && b.isInfected()) {
                    crossOverFlag = true;
                    Thread t = new Thread(new CrossoverAndMutation(a, b, crossOverFlag));
                    t.start();
                } // If only A is infected then check whether A has new variant, infect B with A's virus.
                else if (a.isInfected()) {
                    // If A has new Variant of Virus add it to the HashMap
                    if (checkNewVariant(latestVariantFitness, a)) {
                        variantCount++;
                        updateFitnessTable(latestVariantFitness, a, variantCount, variantName);
                        latestVariantFitness = a.getVirusGene().getFitness();
                        RuntimeAttributes.virusvariantLog.put(a.getVirusGene().getName(), latestVariantFitness);
                    }
                    RuntimeAttributes.naiveCount--;
                    RuntimeAttributes.infectedCount++;
                    b.infect(a.getVirusGene());
                    b.setDayOfInfection(numberOfDays);
                    Thread t = new Thread(new CrossoverAndMutation(a, b, crossOverFlag));
                    t.start();

                }// If only B is infected then check whether B has new variant, infect A with B's virus. 
                else if (b.isInfected()) {
                    if (checkNewVariant(latestVariantFitness, b)) {
                        variantCount++;
                        updateFitnessTable(latestVariantFitness, b, variantCount, variantName);
                        latestVariantFitness = b.getVirusGene().getFitness();
                        RuntimeAttributes.virusvariantLog.put(b.getVirusGene().getName(), latestVariantFitness);
                    }
                    RuntimeAttributes.naiveCount--;
                    RuntimeAttributes.infectedCount++;
                    a.infect(b.getVirusGene());
                    a.setDayOfInfection(numberOfDays);
                    Thread t = new Thread(new CrossoverAndMutation(b, a, crossOverFlag));
                    t.start();
                }
            }
            this.infectedCount.setText(String.valueOf(RuntimeAttributes.infectedCount));
            this.deathCount.setText(String.valueOf(RuntimeAttributes.deathCount));
            this.recoveredCount.setText(String.valueOf(RuntimeAttributes.recoveredCount));

            
//            previousDeaths= RuntimeAttributes.deathCount-previousDeaths;
//            previousInfections= RuntimeAttributes.infectedCount-previousInfections;
//            previousRecovered= RuntimeAttributes.recoveredCount-previousRecovered;
            
            if(numberOfDays%100==0){
                this.deathDataset.addToDataset("Deaths", RuntimeAttributes.deathCount, numberOfDays);
                this.infectedDataset.addToDataset("Infected", RuntimeAttributes.infectedCount, numberOfDays);
                this.recoveredDataset.addToDataset("Recovered" ,RuntimeAttributes.recoveredCount, numberOfDays);
            }
            
        }

        displayStatistics();
        displayVirusData();
        displayGenotypePopulation(population);
        displayGenotypeDeathStatistics(population);
        displayGenotypeRecoveredStatistics(population);
        displayGenotypeInfectedStatistics(population);

    }

    // Function to check if the person's containing Virus has greater fitness than the latestVariantFitness by k_factor then return true;
    public static boolean checkNewVariant(double latestVariantFitness, Person p) {
        double currentVirusFitness = p.getVirusGene().getFitness();
        if (currentVirusFitness - latestVariantFitness > RuntimeAttributes.k_factor) {
            return true;
        } else {
            return false;
        }
    }

    // Function to Update the fitness Table and its probability of affecting host based on the increase in k_factor
    public static void updateFitnessTable(double latestVariantFitness, Person a, int variantCount, String variantName) {
        double currentVirusFitness = a.getVirusGene().getFitness();
        double factorInc = currentVirusFitness - latestVariantFitness;
        double factorPercentInc = (factorInc / latestVariantFitness) * 100;
        ArrayList<Double> genomeValues = RuntimeAttributes.fitnessTable.get(variantName);
        double A1 = genomeValues.get(0) * (1 + (factorPercentInc / 100));
        double A2 = genomeValues.get(1) * (1 + (factorPercentInc / 100));
        double B1 = genomeValues.get(2) * (1 + (factorPercentInc / 100));
        double B2 = genomeValues.get(3) * (1 + (factorPercentInc / 100));
        double A1R = genomeValues.get(4) * (1 + (factorPercentInc / 100));
        double A2R = genomeValues.get(5) * (1 + (factorPercentInc / 100));
        double B1R = genomeValues.get(6) * (1 + (factorPercentInc / 100));
        double B2R = genomeValues.get(7) * (1 + (factorPercentInc / 100));
        double A1V = genomeValues.get(8) * (1 + (factorPercentInc / 100));
        double A2V = genomeValues.get(9) * (1 + (factorPercentInc / 100));
        double B1V = genomeValues.get(10) * (1 + (factorPercentInc / 100));
        double B2V = genomeValues.get(11) * (1 + (factorPercentInc / 100));

        a.getVirusGene().setName("V" + variantCount);
        RuntimeAttributes.fitnessTable.put(a.getVirusGene().getName(), new ArrayList<>(Arrays.asList(A1, A2, B1, B2, A1R, A2R, B1R, B2R, A1V, A2V, B1V, B2V)));

    }

    // Function to display VirusesVariant Log
    public static void displayVirusData() {
        System.out.println("-----------------VIRUS DATA-------------------");

        System.out.println("Virus Variant Log");

        for (String key : RuntimeAttributes.virusvariantLog.keySet()) {
            System.out.println(key + " = " + RuntimeAttributes.virusvariantLog.get(key).toString());
        }
        System.out.println("----------------------------------------------");

    }

    // Function to display Overall Statistics at the end of 2 year time period
    private static void displayStatistics() {
        System.out.println("-----------------HUMAN DATA-------------------");
        System.out.println("No of people Naive:" + RuntimeAttributes.naiveCount);
        System.out.println("No of people Infected: " + RuntimeAttributes.infectedCount);
        System.out.println("No of people Dead: " + RuntimeAttributes.deathCount);
        System.out.println("No of Vaccinated people Dead: " + RuntimeAttributes.vaccinatedDeathCount);
        System.out.println("No of Naive people Dead: " + RuntimeAttributes.naiveDeathCount);
        System.out.println("No of Recovered people Dead: " + RuntimeAttributes.recoveredDeathCount);
        System.out.println("No of people Recovered: " + (RuntimeAttributes.recoveredCount - RuntimeAttributes.deathCount));
        System.out.println("----------------------------------------------");
    }

    // function to dispaly Death Statistics based on Genotype of human
    private static List<Integer> displayGenotypeDeathStatistics(Population population) {
        int A1GenotypeCount = 0;
        int B1GenotypeCount = 0;
        int A2GenotypeCount = 0;
        int B2GenotypeCount = 0;
        for (Person per : population.getPersonList()) {
            if (per.getCategory() == "A1" && !per.isAlive()) {
                A1GenotypeCount++;
            } else if (per.getCategory() == "A2" && !per.isAlive()) {
                A2GenotypeCount++;
            } else if (per.getCategory() == "B1" && !per.isAlive()) {
                B1GenotypeCount++;
            } else if (per.getCategory() == "B2" && !per.isAlive()) {
                B2GenotypeCount++;
            }
        }
        return new ArrayList<>(Arrays.asList(A1GenotypeCount,B1GenotypeCount,A2GenotypeCount, B2GenotypeCount));
//        System.out.println("-----------------HUMAN Deaths Based on Genotype-------------------");
//        System.out.println("A1 Genotype: " + A1GenotypeCount);
//        System.out.println("A2 Genotype: " + A2GenotypeCount);
//        System.out.println("B1 Genotype: " + B1GenotypeCount);
//        System.out.println("B2 Genotype: " + B2GenotypeCount);
//        System.out.println("------------------------------------------------------------------");

    }

    // function to dispaly Recovery Statistics based on Genotype of human
    private static List<Integer> displayGenotypeRecoveredStatistics(Population population) {
        int A1GenotypeCount = 0;
        int B1GenotypeCount = 0;
        int A2GenotypeCount = 0;
        int B2GenotypeCount = 0;
        for (Person per : population.getPersonList()) {
            if (per.getCategory() == "A1" && per.isRecovered()) {
                A1GenotypeCount++;
            } else if (per.getCategory() == "A2" && per.isRecovered() && per.isAlive()) {
                A2GenotypeCount++;
            } else if (per.getCategory() == "B1" && per.isRecovered() && per.isAlive()) {
                B1GenotypeCount++;
            } else if (per.getCategory() == "B2" && per.isRecovered() && per.isAlive()) {
                B2GenotypeCount++;
            }
        }
        return new ArrayList<>(Arrays.asList(A1GenotypeCount,B1GenotypeCount,A2GenotypeCount, B2GenotypeCount));
//        System.out.println("-----------------HUMAN Recovery Based on Genotype-------------------");
//        System.out.println("A1 Genotype: " + A1GenotypeCount);
//        System.out.println("A2 Genotype: " + A2GenotypeCount);
//        System.out.println("B1 Genotype: " + B1GenotypeCount);
//        System.out.println("B2 Genotype: " + B2GenotypeCount);
//        System.out.println("------------------------------------------------------------------");
    }

    // function to dispaly division of different genotypes in the population
    private static List<Integer> displayGenotypePopulation(Population population) {
        int A1GenotypeCount = 0;
        int B1GenotypeCount = 0;
        int A2GenotypeCount = 0;
        int B2GenotypeCount = 0;
        for (Person per : population.getPersonList()) {
            if (per.getCategory() == "A1") {
                A1GenotypeCount++;
            } else if (per.getCategory() == "A2") {
                A2GenotypeCount++;
            } else if (per.getCategory() == "B1") {
                B1GenotypeCount++;
            } else if (per.getCategory() == "B2") {
                B2GenotypeCount++;
            }
        }
        return new ArrayList<>(Arrays.asList(A1GenotypeCount,B1GenotypeCount,A2GenotypeCount, B2GenotypeCount));
//        System.out.println("-----------------HUMAN Division Based on Genotype-------------------");
//        System.out.println("A1 Genotype: " + A1GenotypeCount);
//        System.out.println("A2 Genotype: " + A2GenotypeCount);
//        System.out.println("B1 Genotype: " + B1GenotypeCount);
//        System.out.println("B2 Genotype: " + B2GenotypeCount);
//        System.out.println("------------------------------------------------------------------");
    }

    // function to dispaly Infected Statistics based on Genotype of human
    private static List<Integer> displayGenotypeInfectedStatistics(Population population) {
        int A1GenotypeCount = 0;
        int B1GenotypeCount = 0;
        int A2GenotypeCount = 0;
        int B2GenotypeCount = 0;
        for (Person per : population.getPersonList()) {
            if (per.getCategory() == "A1" && per.isInfected()) {
                A1GenotypeCount++;
            } else if (per.getCategory() == "A2" && per.isInfected()) {
                A2GenotypeCount++;
            } else if (per.getCategory() == "B1" && per.isInfected()) {
                B1GenotypeCount++;
            } else if (per.getCategory() == "B2" && per.isInfected()) {
                B2GenotypeCount++;
            }
        }
        
        return new ArrayList<>(Arrays.asList(A1GenotypeCount,B1GenotypeCount,A2GenotypeCount, B2GenotypeCount));
//        System.out.println("-----------------HUMAN Infected Based on Genotype-------------------");
//        System.out.println("A1 Genotype: " + A1GenotypeCount);
//        System.out.println("A2 Genotype: " + A2GenotypeCount);
//        System.out.println("B1 Genotype: " + B1GenotypeCount);
//        System.out.println("B2 Genotype: " + B2GenotypeCount);
//        System.out.println("------------------------------------------------------------------");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startVaccinationBtn = new javax.swing.JButton();
        stopSimulationBtn = new javax.swing.JButton();
        reset = new javax.swing.JButton();
        viewStatsBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        dayCounter = new javax.swing.JLabel();
        threeX = new javax.swing.JButton();
        oneX = new javax.swing.JButton();
        twoX = new javax.swing.JButton();
        recoveredCount = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        infectedCount = new javax.swing.JLabel();
        deathCount = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        startVaccinationBtn.setBackground(new java.awt.Color(255, 193, 0));
        startVaccinationBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        startVaccinationBtn.setForeground(new java.awt.Color(102, 102, 102));
        startVaccinationBtn.setText("Start Vaccination");
        startVaccinationBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startVaccinationBtnActionPerformed(evt);
            }
        });
        add(startVaccinationBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 290, 140, -1));

        stopSimulationBtn.setBackground(new java.awt.Color(255, 193, 0));
        stopSimulationBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        stopSimulationBtn.setForeground(new java.awt.Color(102, 102, 102));
        stopSimulationBtn.setText("Stop Simulation");
        stopSimulationBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopSimulationBtnActionPerformed(evt);
            }
        });
        add(stopSimulationBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 330, 140, -1));

        reset.setBackground(new java.awt.Color(255, 193, 0));
        reset.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        reset.setForeground(new java.awt.Color(102, 102, 102));
        reset.setText("Reset Simulation");
        reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });
        add(reset, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 410, 140, -1));

        viewStatsBtn.setBackground(new java.awt.Color(255, 193, 0));
        viewStatsBtn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        viewStatsBtn.setForeground(new java.awt.Color(102, 102, 102));
        viewStatsBtn.setText("View Statistics");
        viewStatsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewStatsBtnActionPerformed(evt);
            }
        });
        add(viewStatsBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 370, 140, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Day Count");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, 350, 60));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("INFO 6205, Northeastern University. Summer, 2021");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 510, -1, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Virus_1.gif"))); // NOI18N
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, -1, -1));

        dayCounter.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        dayCounter.setForeground(new java.awt.Color(102, 102, 102));
        dayCounter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dayCounter.setText("365");
        add(dayCounter, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 110, 60));

        threeX.setBackground(new java.awt.Color(255, 193, 0));
        threeX.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        threeX.setForeground(new java.awt.Color(102, 102, 102));
        threeX.setText("3x");
        threeX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                threeXActionPerformed(evt);
            }
        });
        add(threeX, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 250, 50, -1));

        oneX.setBackground(new java.awt.Color(255, 193, 0));
        oneX.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        oneX.setForeground(new java.awt.Color(102, 102, 102));
        oneX.setText("1x");
        oneX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oneXActionPerformed(evt);
            }
        });
        add(oneX, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, 50, -1));

        twoX.setBackground(new java.awt.Color(255, 193, 0));
        twoX.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        twoX.setForeground(new java.awt.Color(102, 102, 102));
        twoX.setText("2x");
        twoX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                twoXActionPerformed(evt);
            }
        });
        add(twoX, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 250, 50, -1));

        recoveredCount.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        recoveredCount.setForeground(new java.awt.Color(102, 102, 102));
        recoveredCount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        recoveredCount.setText("0");
        add(recoveredCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 210, 50, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Number of People Recovered     ");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, 200, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Number of Deaths  ");
        add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 120, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("Number of Infected People");
        add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 170, -1));

        infectedCount.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        infectedCount.setForeground(new java.awt.Color(102, 102, 102));
        infectedCount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infectedCount.setText("0");
        add(infectedCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, 50, -1));

        deathCount.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        deathCount.setForeground(new java.awt.Color(102, 102, 102));
        deathCount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        deathCount.setText("0");
        add(deathCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 160, 50, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void startVaccinationBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startVaccinationBtnActionPerformed
        // TODO add your handling code here:
        this.startVaccinationFlag=true;
    }//GEN-LAST:event_startVaccinationBtnActionPerformed

    private void stopSimulationBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopSimulationBtnActionPerformed
        // TODO add your handling code here:
        this.stopSim = true;
        this.stopSimulationBtn.setEnabled(false);
        this.reset.setEnabled(true);
    }//GEN-LAST:event_stopSimulationBtnActionPerformed

    private void viewStatsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewStatsBtnActionPerformed
        // TODO add your handling code here:
        
        if (this.numberOfDays == 730) {
            this.body.removeAll();
            StatisticsPanel sp = new StatisticsPanel(body,this.displayGenotypeDeathStatistics(population),displayGenotypeRecoveredStatistics(population),displayGenotypePopulation(population),this.deathDataset,this.infectedDataset,this.recoveredDataset);
            this.body.add("StatisticsPanel", sp);
            CardLayout crdLyt = (CardLayout) body.getLayout();
            crdLyt.next(body);
        }
        else
            JOptionPane.showMessageDialog(null, "Statistics will be available after simulation is completed.", "Simulation not complete", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_viewStatsBtnActionPerformed

    private void threeXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_threeXActionPerformed
        // TODO add your handling code here:
        this.timer = 100;
        this.threeX.setEnabled(false);
        this.oneX.setEnabled(true);
        this.twoX.setEnabled(true);
    }//GEN-LAST:event_threeXActionPerformed

    private void oneXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oneXActionPerformed
        // TODO add your handling code here:
        this.timer = 500;
        this.oneX.setEnabled(false);
        this.twoX.setEnabled(true);
        this.threeX.setEnabled(true);
    }//GEN-LAST:event_oneXActionPerformed

    private void twoXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_twoXActionPerformed
        // TODO add your handling code here:
        this.timer = 250;
        this.twoX.setEnabled(false);
        this.oneX.setEnabled(true);
        this.threeX.setEnabled(true);
    }//GEN-LAST:event_twoXActionPerformed

    private void resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetActionPerformed
        // TODO add your handling code here:
        this.numberOfDays = 0;
        this.body.removeAll();
        MainMenuPanel mmp = new MainMenuPanel(body);
        this.body.add("MainMenuJPanel", mmp);
        CardLayout crdLyt = (CardLayout) body.getLayout();
        crdLyt.next(body);
    }//GEN-LAST:event_resetActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dayCounter;
    private javax.swing.JLabel deathCount;
    private javax.swing.JLabel infectedCount;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JButton oneX;
    private javax.swing.JLabel recoveredCount;
    private javax.swing.JButton reset;
    private javax.swing.JButton startVaccinationBtn;
    private javax.swing.JButton stopSimulationBtn;
    private javax.swing.JButton threeX;
    private javax.swing.JButton twoX;
    private javax.swing.JButton viewStatsBtn;
    // End of variables declaration//GEN-END:variables

    private void starter() {
        Runnable task = () -> {
            this.startSimulation();
        };
        Thread worker = new Thread(task);
        worker.start();
    }

}
