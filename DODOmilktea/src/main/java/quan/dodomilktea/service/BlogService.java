package quan.dodomilktea.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quan.dodomilktea.dto.admin.BlogRequestDto;
import quan.dodomilktea.jwt.JwtTokenUlti;
import quan.dodomilktea.model.Blog;
import quan.dodomilktea.repo.BlogRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
@Service
public class BlogService {
    @Autowired
    BlogRepository blogRepo;
    @Autowired
    AccountService accountService;
    @Autowired
    JwtTokenUlti jwtTokenUtil;
    Logger logger = LoggerFactory.getLogger(BlogService.class);

    public List<Blog> getAllBlogs() {
        return blogRepo.findAll();
    }

    public Blog saveBlog(Blog blog) {
        blog.setEnabled(true);
        return blogRepo.save(blog);
    }

    public Blog insertBlog(BlogRequestDto blogInsert, HttpServletRequest request) {
        Blog blogResponse = new Blog();

        blogResponse = setData(blogResponse, blogInsert, request);
        blogResponse.setCreated_at(new Date());

        try {
            blogResponse = blogRepo.save(blogResponse);
        } catch(Exception e) {
            logger.error(e.getMessage());
            blogResponse = null;
        }
        return blogResponse;
    }

    public Blog updateBlog(BlogRequestDto blogUpdate, String id, HttpServletRequest request) {
        Blog blogResponse = blogRepo.findById(id).orElse(null);
        if(blogResponse == null || blogResponse.isEnabled() == false) { return null; }

        blogResponse = setData(blogResponse, blogUpdate, request);

        try {
            blogResponse = blogRepo.save(blogResponse);
        } catch(Exception e) {
            logger.error(e.getMessage());
            blogResponse = null;
        }
        return blogResponse;
    }

    public boolean deleteBlog(String id) {
        Blog blogDelete = blogRepo.findById(id).orElse(null);
        if(blogDelete == null || blogDelete.isEnabled() == false) { return false; }

        blogDelete.setEnabled(false);
        try {
            blogRepo.save(blogDelete);
        } catch(Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    private Blog setData(Blog blogResponse, BlogRequestDto blogInsert, HttpServletRequest request) {
        blogResponse.setTitle(blogInsert.getTitle());
        blogResponse.setContent(blogInsert.getContent());
        blogResponse.setImage_url(blogInsert.getImageUrl());
        blogResponse.setEnabled(true);

        String token = jwtTokenUtil.getAccessToken(request);
        String[] subject = jwtTokenUtil.getSubject(token).split(",");
        blogResponse.setAccount(accountService.getAccById(subject[0]));
        return blogResponse;
    }
}
