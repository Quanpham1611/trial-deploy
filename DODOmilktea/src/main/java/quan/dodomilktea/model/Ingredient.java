package quan.dodomilktea.model;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id", length = 10)
    private String ingredient_id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "manufactoring_date")
    private Date manufactoring_date;

    @Column(name = "expired_date")
    private Date expired_date;

    @Column(name = "cost")
    private int cost;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "last_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date last_update;

    @Column(name = "note", length = 255)
    private String note;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    @JsonIgnore
    private Account manager_account;

    @Column(name = "enabled")
    private boolean enabled;
}
