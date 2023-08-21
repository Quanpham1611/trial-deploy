package quan.dodomilktea.model;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "workday")
public class WorkDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_day")
    private LocalDate work_day;

    @Column(name = "is_holiday")
    private boolean is_holiday;

    @Column(name = "enabled")
    private boolean enabled;

    @OneToMany(mappedBy = "staff")
    @JsonIgnore
    private Set<StaffWorkDay> staffWorkDays;
}
