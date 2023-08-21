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
@Table(name = "staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_number", length = 12)
    private String id_number;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "phone", length = 11)
    private String phone;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "staff_role", length = 30, nullable = false)
    private String staff_role;

    @Column(name = "day_of_birth")
    private LocalDate day_of_birth;

    @Column(name = "start_date", nullable = false)
    private LocalDate start_date;

    @ManyToOne
    @JoinColumn(name = "salary_id")
    @JsonIgnore
    private Salary salary;

    @ManyToOne
    @JoinColumn(name = "salary_ot_id")
    @JsonIgnore
    private Salary ot_salary;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    @JsonIgnore
    private Staff manager;

    @OneToMany(mappedBy = "manager")
    @JsonIgnore
    private Set<Staff> managedStaffs;

    @ManyToOne
    @JoinColumn(name = "manager_acc_id")
    @JsonIgnore
    private Account manager_account;

    @ManyToOne
    @JoinColumn(name = "deliver_acc_id")
    @JsonIgnore
    private Account deliver_account;

    @Column(name = "enabled")
    private boolean enabled;

    @OneToMany(mappedBy = "workDay")
    @JsonIgnore
    Set<StaffWorkDay> staffWorkDays;
}
