package quan.dodomilktea.model;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "add_on")
public class AddOn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "add_on_id", length = 10)
    private String add_on_id;

    @Column(name = "name", length = 40)
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "cost")
    private int cost;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany(mappedBy = "addOn", fetch = FetchType.LAZY)
    @JsonIgnore
    List<CustomMilkTea> customMilkTeas;
}
