/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author sumit
 */
import java.util.Random;

public class CrossoverAndMutation implements Runnable {

    Person a;
    Person b;
    boolean crossoverFlag;

    public CrossoverAndMutation(Person a, Person b, boolean crossoverFlag) {
        this.a = a;
        this.b = b;
        this.crossoverFlag = crossoverFlag;
    }


    @Override
    public void run() {
        // If the corssover flag is true perform crossover else perfrom mutaion 
        if (!this.crossoverFlag) {
            if (a.isVaccinated()) {
                a.getVirusGene().mutateAfterVaccination();
            } else {
                a.getVirusGene().mutate();
            }
            if (b.isVaccinated()) {
                b.getVirusGene().mutateAfterVaccination();
            } else {
                b.getVirusGene().mutate();

            }
            fightVirus(a, b);

        } else {
            VirusGene v1 = a.getVirusGene();
            VirusGene v2 = b.getVirusGene();
            VirusGene newVirus;
            Random random = new Random();
            int flag = random.nextInt(2);
            if (flag == 1) {
                newVirus = v1.crossOver(v2);
                a.setVirusGene(newVirus);
            } else {
                newVirus = v2.crossOver(v1);
                b.setVirusGene(newVirus);
            }
            if (a.isVaccinated()) {
                a.getVirusGene().mutateAfterVaccination();
            } else {
                a.getVirusGene().mutate();
            }
            if (b.isVaccinated()) {
                b.getVirusGene().mutateAfterVaccination();
            } else {
                b.getVirusGene().mutate();
            }
            fightVirus(a, b);
        }

    }
    
    // Function to let Person A and Person B fight Virus after mutation or crossover
    public void fightVirus(Person a, Person b) {
        Runnable task1 = () -> {
            a.fightVirus();
        };
        Runnable task2 = () -> {
            b.fightVirus();
        };
        Thread t1 = new Thread(task1);
        t1.start();
        Thread t2 = new Thread(task2);
        t2.start();
    }
}
