package com.code.challenge.repository;

import com.code.challenge.entity.Email;
import com.code.challenge.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends CrudRepository<Email, Long> {
    @Query("update Email em set em.mail =:email where em.user =:user and em.id =:id")
    Email update(Email email, User user, long id);
}
