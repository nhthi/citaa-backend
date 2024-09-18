package com.citaa.citaa.repository;

import com.citaa.citaa.model.Chat;
import com.citaa.citaa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    public List<Chat> findByUsersId(int userId);

    @Query("select c from Chat c where :user member of c.users and :reqUser member of c.users")
    public Chat findChatByUsersId(@Param("user") User user, @Param("reqUser")User reqUser);

}
