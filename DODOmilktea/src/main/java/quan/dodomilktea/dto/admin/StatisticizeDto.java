package quan.dodomilktea.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticizeDto {
    private int totalFunds;
    private int totalRevenue;
    private int totalProfit;
}