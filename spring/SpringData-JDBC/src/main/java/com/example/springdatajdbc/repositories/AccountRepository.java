package com.example.springdatajdbc.repositories;

import com.example.springdatajdbc.models.Account;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

// The first generic type value is the type of
// the model class representing the table. The
// second is the type of the primary key field.
public interface AccountRepository extends CrudRepository<Account, Integer> {
    @Query("SELECT * FROM account WHERE name = :name")
    List<Account> findAccountsByName(String name);

    // We annotate the methods that define operations that change data with the @Modifying annotation.
    @Modifying
    @Query("UPDATE account SET amount = :amount WHERE id = :id")
    void changeAmount(long id, BigDecimal amount);


}
