import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public abstract class Person implements Serializable //superclass

{
    //attributes
    private String id; // unique id to each person
    private String name;
    public static List<String> idNumbers = new ArrayList<>(); //contains the ids of all people in the system
    private static int lastId; // last ID in the idNumbers list

    //constructors

    public Person(String name)
    {
        setName(name);
        this.id = setId();

    }

    public Person() // overloading constructor
    {   }

    // accessors and mutators (getters and setters)
    public String getId()
    {
        return this.id;
    }

    private String setId()
    {
        if (idNumbers.isEmpty())
        {
            lastId = 0;
            idNumbers.add(String.format("%04d", 0)); // id is a string of length 4 with preceding zeroes
        }
        else
        {
            lastId++;
            idNumbers.add(String.format("%04d", lastId));
        }
        return idNumbers.get(lastId);
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    //toString - polymorphism
    public String toString()
    {
        return "ID: " + this.getId() + " Name: " + this.getName();
    }

}
