package quan.dodomilktea.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quan.dodomilktea.service.BlogService;

@RestController
@RequestMapping("/home")
@CrossOrigin(origins ="http://localhost:3000")
public class BlogController {
    @Autowired
    BlogService blogService;

    @GetMapping("/all-blogs")
    public ResponseEntity<?> listBlogs() {
        return ResponseEntity.ok(blogService.getAllBlogs());
    }
}
