package quan.dodomilktea.model;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name = "custom_milk_tea")
public class CustomMilkTea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custom_milk_tea_id", length = 10)
    private String custom_milk_tea_id;

    @ManyToOne
    @JoinColumn(name = "milktea_id")
    private MilkTea milkTea;

    @Column(name = "ice_amount")
    private int ice_amount;

    @Column(name = "sugar_amount")
    private int sugar_amount;

    @Column(name = "size", length = 2)
    private String size;

    @Column(name = "total_cost")
    private int total_cost;

    @Column(name = "total_price")
    private int total_price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @ManyToMany
    @JoinTable(
            schema = "custom_milk_tea_add_on",
            joinColumns = @JoinColumn(name = "custom_milk_tea_id"),
            inverseJoinColumns = @JoinColumn(name = "add_on_id"))
    @JsonIgnoreProperties
    List<AddOn> addOn;
}
