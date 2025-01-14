package com.citaa.citaa.repository;

import com.citaa.citaa.model.Startup;
import com.citaa.citaa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.account.username=:username")
    public User findByUsername(@Param("username") String username);

    public User findByEmail(String email);

    @Query("Select a from User a where a.account.role = 'ROLE_ADMIN'")
    public List<User> findAllAdmin();

    ;
    @Query("SELECT u FROM User u " +
            "WHERE (u.account.role = :role) and" +
            " (:province = '0'  OR u.address LIKE %:province%)" +
            "and (:query = '0' or u.fullName like %:query%)" +
            "and (:valid ='0' or u.valid = :valid)")
    List<User> findByProvinceAndFieldAndQuery(@Param("province") String province , String role, String query,String valid);
}
