package quan.dodomilktea.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quan.dodomilktea.dto.admin.BlogRequestDto;
import quan.dodomilktea.dto.customer.MessageDto;
import quan.dodomilktea.model.Blog;
import quan.dodomilktea.service.BlogService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/admin/blog")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminBlogController {
    @Autowired
    BlogService blogService;

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAllBlogs(){
        List<Blog> blogs = blogService.getAllBlogs();
        if(blogs == null){
            throw new NullPointerException();
        }
        return ResponseEntity.ok(blogs);
    }

    @PostMapping("/insert")
    public ResponseEntity<Object> insertBlog(@RequestBody BlogRequestDto blogInsert, HttpServletRequest request){
        Blog blogResponse = blogService.insertBlog(blogInsert, request);
        if(blogResponse == null){
            return ResponseEntity.ok(new MessageDto("Insert blog unsuccessful"));
        }
        return ResponseEntity.ok(blogResponse);
    }

    @PutMapping("/update/{blogId}")
    public ResponseEntity<Object> updateBlog(@RequestBody BlogRequestDto contactUpdate,@PathVariable String blogId, HttpServletRequest request){
        Blog blogResponse = blogService.updateBlog(contactUpdate, blogId, request);
        if(blogResponse == null){
            return ResponseEntity.ok(new MessageDto("Update unsuccessfull"));
        }
        return ResponseEntity.ok(blogResponse);
    }

    @PutMapping("/delete/{blogId}")
    public ResponseEntity<Object> deleteContact(@PathVariable String blogId){
        boolean isDeleted = blogService.deleteBlog(blogId);
        if(isDeleted){
            return ResponseEntity.ok(new MessageDto("Delete successfull"));
        }
        return ResponseEntity.ok(new MessageDto("Delete unsuccessfull"));
    }
}
