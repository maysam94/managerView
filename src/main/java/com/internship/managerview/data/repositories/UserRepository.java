package com.internship.managerview.data.repositories;

import com.internship.managerview.data.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void updatePassword(@Param("id") int id, @Param("password") String password);

    @Query("SELECT u.password FROM User u WHERE u.id = :id")
    String findPasswordById(@Param("id") int id);

    /**
     * Finds users in the database whose firstname and/or lastname and/or prefixes start with the requested name.
     * In order:
     * Sorts them by their first name if they have been found by their first name,
     * Sorts them by their last name if they have been found by their last name,
     * Sorts them by their first name if they have been found by their prefixes.
     *
     * @param name The name to find the users by
     * @return The found users
     * @author Anne Butter
     */
    @Query("""
            select u
            from User u
            where upper(u.firstName) like upper(:name||'%') or
            upper(u.lastName) like upper(:name||'%') or
            upper(u.firstName || ' ' || u.lastName) like upper(:name||'%') or
            upper(u.firstName || ' ' || u.prefixes || ' ' || u.lastName) like upper(:name||'%') or
            upper(u.prefixes || ' ' || u.lastName) like upper(:name ||'%')
            order by case when upper(u.firstName) like upper(:name||'%') then 0
                          when upper(u.lastName) like upper(:name||'%') then 1
                          else 2 end
                   , case when upper(u.lastName) like upper(:name||'%') and upper(u.firstName) not like upper(:name||'%') then u.lastName
                          else u.firstName end
                   , u.lastName
            limit 100
            """)
    Iterable<User> findAllUsersByName(String name);
}
