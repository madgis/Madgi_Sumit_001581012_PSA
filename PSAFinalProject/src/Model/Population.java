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
import java.util.List;

public class Population {

    private List<Person> personList;

    public Population(int count){
        this.personList= new ArrayList<>(count);
        initializePopulation(count);
    }

    public void initializePopulation(int count){
        while(this.personList.size()<=count)
            this.personList.add(new Person());
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }
    
    

}

