/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import junit.framework.TestCase;

/**
 *
 * @author sumit
 */
public class VirusGeneTest extends TestCase {
    
    VirusGene virusGene;
    
    public VirusGeneTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        virusGene= new VirusGene();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    /**
     * Test of mutate method, of class VirusGene.
     */
    public void testMinFitnessAfterMutattion() {
        double fitness = virusGene.getFitness();
        double expected=0;
        assertTrue(fitness>expected);
    }
    
    /**
     * Test of mutate method, of class VirusGene.
     */
    public void testMaxFitnessAfterMutattion() {
        double fitness = virusGene.getFitness();
        double expected=100;
        assertTrue(fitness<expected);
    }

    /**
     * Test of mutateAfterVaccination method, of class VirusGene.
     */
    public void testMutateAfterVaccination() {
        double fitness = virusGene.getFitness();
        double expected=100;
        assertTrue(fitness<expected);
    }

    /**
     * Test of crossOver method, of class VirusGene.
     */
    public void testCrossOver() {
    }

    /**
     * Test of getGenome method, of class VirusGene.
     */
    public void testGetGenome() {
    }

    /**
     * Test of setFitness method, of class VirusGene.
     */
    public void testSetFitness_double() {
    }

    /**
     * Test of getFitness method, of class VirusGene.
     */
    public void testGetFitness() {
    }

    /**
     * Test of toString method, of class VirusGene.
     */
    public void testToString() {
    }

    /**
     * Test of compareTo method, of class VirusGene.
     */
    public void testCompareTo() {
    }
    
}
