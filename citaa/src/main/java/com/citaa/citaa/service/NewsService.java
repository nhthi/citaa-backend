package com.citaa.citaa.service;

import com.citaa.citaa.model.News;
import com.citaa.citaa.model.Project;
import com.citaa.citaa.model.User;
import com.citaa.citaa.repository.NewsRepository;
import com.citaa.citaa.repository.UserRepository;
import com.citaa.citaa.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                .updateAt(LocalDateTime.now())
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

    public Page<News> filterNews(String field, int year, int pageSize,int pageNumber){
        List<News> newss = newsRepository.filterNews(field, year);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), newss.size());

        List<News> pageContent = newss.subList(startIndex, endIndex);
        Page<News> filteredNews = new PageImpl<>(pageContent, pageable, newss.size());
        return filteredNews;
    }

    public Page<News> filterNewsAdmin(List<String> fields, int year, int pageSize,int pageNumber){
        List<News> newss = newsRepository.filterNewsAdmin(year);

        if (fields!=null && !fields.isEmpty()) {
            newss = newss.stream().
                    filter(
                            news -> fields.stream().anyMatch((field -> field.equalsIgnoreCase(news.getType())))
                    ).collect(Collectors.toList());
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), newss.size());

        List<News> pageContent = newss.subList(startIndex, endIndex);
        Page<News> filteredNews = new PageImpl<>(pageContent, pageable, newss.size());
        return filteredNews;
    }

    public List<News> getLatestNewsSimilarTo(int id, String type) {
        return newsRepository.findLatestByType( id, type, PageRequest.of(0, 3));
    }

    public ApiResponse deleteNewsById(int id) throws Exception {
        ApiResponse res = new ApiResponse();
        News news = getNewsById(id);
        newsRepository.deleteById(id);
        res.setMessage("Xóa thành công");
        res.setStatus(200);
        return res;
    }

    public News updateNewsById(int id,News req) throws Exception {
        News news = getNewsById(id);
        if(req.getTitle()!=null && !req.getTitle().equals(news.getTitle())){
            news.setTitle(req.getTitle());
        }
        if(req.getType()!=null && !req.getType().equals(news.getType())){
            news.setType(req.getType());
        }
        if(req.getContent()!=null && !req.getContent().equals(news.getContent())){
            news.setContent(req.getContent());
        }
        if(req.getThumbnail()!=null && !req.getThumbnail().equals(news.getThumbnail())){
            news.setThumbnail(req.getThumbnail());
        }
        news.setUpdateAt(LocalDateTime.now());
        return newsRepository.save(news);
    }
}
