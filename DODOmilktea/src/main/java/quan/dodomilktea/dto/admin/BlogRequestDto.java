package quan.dodomilktea.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor@AllArgsConstructor
public class BlogRequestDto {
    private String title, content, imageUrl;
}
