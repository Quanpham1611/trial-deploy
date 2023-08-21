package quan.dodomilktea.dto.shipper;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkDayDto {
    private LocalDate workDay;
    private boolean isHoliday;
    private float hours;
}
