/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author sumit
 */
public class PersonTest extends TestCase {
    
    Person person;
    
    public PersonTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        person= new Person();
        
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getCategory method, of class Person.
     */
    public void testGetCategory() {
        List category =Arrays.asList("A1", "B1", "A2", "B2");
        //person.setCategory("asdassa");
        assertTrue(category.contains(person.getCategory()));
        
    }

    /**
     * Test of getGender method, of class Person.
     */
    public void testGetGender() {
        List category =Arrays.asList("M", "F");
        //person.setCategory("asdassa");
        assertTrue(category.contains(person.getGender()));
    }
    
    /**
     * Test of fitness value, of class Person.
     */
    public void testMaxFitness() {
        double fitness= person.getFitness();
        double expected=17640;
        assertTrue(fitness<expected);
    }

    /**
     * Test of fitness value, of class Person.
     */
    public void testMinFitness() {
        double fitness= person.getFitness();
        double expected=13650;
        assertTrue(fitness>expected);
    }
}
