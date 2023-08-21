package quan.dodomilktea.dto.customer;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomOrderDto {
    private String orderId;
    private String note;
    private String phone;
    private List<CustomMilkTeaDto> customMilkTeaDtos;
}