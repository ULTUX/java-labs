package pl.edu.pwr.lab7.jpa.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    @Query("select p from Person p where p.id in " +
            "(select pa.person.id from Payment pa where pa.amount >= " +
                "(select installment.amount from Installment installment where installment.id = pa.installment.id))")
    List<Person> getPending();

}
