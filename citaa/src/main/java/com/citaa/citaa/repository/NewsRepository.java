package com.citaa.citaa.repository;

import com.citaa.citaa.model.News;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {

    @Query("select  n from News n where (:field = 'ALL' or n.type=:field) and (:year = 0 or year(n.createAt)=:year)")
    public List<News> filterNews(String field, int year);

    @Query("select  n from News n where (:year = 0 or year(n.createAt)=:year) and (:adminReply = -1 or n.admin.id = :adminReply)")
    public List<News> filterNewsAdmin( int year,int adminReply);

    @Query("select n from News n where n.type = :type and n.id != :id order by n.createAt desc")
    public List<News> findLatestByType(int id, String type, Pageable pageable);
}