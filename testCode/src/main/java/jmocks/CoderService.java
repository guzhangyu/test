package jmocks;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CoderService {

    @Value("${coder.service.desc}")
    private String desc;

    public String showWork(String work){
        return work;
    }

    public int showSalary(int salary){
        return salary;
    }

    public String getDesc(){
        return desc;
    }

    public String getPersonName(Person person){
        return person.getName();
    }
}
