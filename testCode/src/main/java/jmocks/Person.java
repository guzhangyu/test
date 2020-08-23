package jmocks;


public class Person {

    private String name;

    private Integer age;

    private Person friend;

    public Person() {
    }

    public Person(String name, Integer age, Person friend) {
        this.name = name;
        this.age = age;
        this.friend = friend;
    }

    @Override
    public boolean equals(Object obj) {
        return this.name.equals(((Person)obj).getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Person getFriend() {
        return friend;
    }

    public void setFriend(Person friend) {
        this.friend = friend;
    }
}
