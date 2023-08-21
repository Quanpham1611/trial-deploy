package quan.dodomilktea.model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id", length = 10)
    private String contact_id;

    @Column(name = "type", length = 20)
    private String type;

    @Column(name = "content", length = 255)
    private String content;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "acc_id")
    private Account account;
}
