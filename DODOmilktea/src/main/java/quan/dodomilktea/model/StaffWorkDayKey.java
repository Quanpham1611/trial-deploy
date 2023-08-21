package quan.dodomilktea.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "staff_workday_key")
public class StaffWorkDayKey implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "staff_id")
    String staff_id;

    @Column(name = "work_day")
    LocalDate work_day;
}