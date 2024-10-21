package com.citaa.citaa.service;

import com.citaa.citaa.model.News;
import com.citaa.citaa.model.User;
import com.citaa.citaa.repository.NewsRepository;
import com.citaa.citaa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    @Autowired
    NewsRepository newsRepository;
    @Autowired
    UserService userService;
    public News createNews(News request,String jwt) throws Exception {
        User user = userService.findByJwt(jwt);
        return newsRepository.save(News.builder()
                .title(request.getTitle())
                .type(request.getType())
                .content(request.getContent())
                .thumbnail(request.getThumbnail())
                .createAt(LocalDateTime.now())
                .admin(user)
                .build());
    }
    public List<News> getListNews(){
        return newsRepository.findAll();
    }

    public News getNewsById(int id) throws Exception {
        Optional<News> news = newsRepository.findById(id);
        if(news.isEmpty()){
            throw new Exception("News not found width id: "+id);
        }
        return news.get();
    }
}
