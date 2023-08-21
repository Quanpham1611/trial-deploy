package quan.dodomilktea.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MilkteaRequestDto {
    private String name;
    private int price;
    private int cost;
    private String imageUrl;
    private String categoryId;
}
