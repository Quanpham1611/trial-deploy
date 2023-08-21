package quan.dodomilktea.dto.customer;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import quan.dodomilktea.model.AddOn;
import quan.dodomilktea.model.MilkTea;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MilkTeaDetailDto {
    private MilkTea milkTea;
    private List<AddOn> allAddOns;
}