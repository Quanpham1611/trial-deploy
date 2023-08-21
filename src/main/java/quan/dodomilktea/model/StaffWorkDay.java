package quan.dodomilktea.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "staffworkday")
public class StaffWorkDay {
    @EmbeddedId
    StaffWorkDayKey id;

    @ManyToOne
    @MapsId("staff_id")
    @JoinColumn(name = "staff_id")
    Staff staff;

    @ManyToOne
    @MapsId("work_day")
    @JoinColumn(name = "work_day")
    WorkDay workDay;

    float hours;

}
