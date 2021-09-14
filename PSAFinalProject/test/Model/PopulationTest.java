/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author sumit
 */
public class PopulationTest extends TestCase {
    
    Population pop;
    
    public PopulationTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        pop= new Population(1000);
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of initializePopulation method, of class Population.
     */
    public void testInitializePopulation() {
        List<Person> persons = pop.getPersonList();
        assertNotNull(persons);
    }
    
    public void testAllPersonNaive(){
        for(Person per: pop.getPersonList()){
            assertTrue(per.isNaive());
        }
    }
    
    public void testAllPersonHasHumanGene(){
        for(Person per: pop.getPersonList()){
            assertNotNull(per.getGene());
        }
    }

    
}
