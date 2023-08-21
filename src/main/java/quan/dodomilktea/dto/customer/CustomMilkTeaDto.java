package quan.dodomilktea.dto.customer;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomMilkTeaDto {
    private String name;
    private int ice_amount;
    private int sugar_amount;
    private String size;
    private int quantity;
    private List<String> addOnName;
}