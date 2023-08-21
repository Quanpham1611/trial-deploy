package quan.dodomilktea.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientRequestDto {
    private String name;
    private String manufactoringDate;
    private String expiredDate;
    private int cost;
    private int quantity;
    private String note;
}
