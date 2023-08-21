package quan.dodomilktea.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "account")
public class Account {
    private static final long OTP_VALID_DURATION = 1*60*1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acc_id", length = 10)
    private String acc_id;

    @Column(name = "email", unique = true, length = 50)
    private String email;

    @Column(name = "password", nullable = false, length = 70)
    private String password;

    @Column(name = "one_time_password")
    private String oneTimePassword;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "otp_requested_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otpRequestedTime;

    @Column(name = "role", nullable = false, length = 40)
    private String role;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "phone", unique = true, length = 10)
    private String phone;

    @Column(name = "address", nullable = false, length = 50)
    private String address;

    @Column(name = "enabled")
    private boolean enabled;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private Set<Blog> blogs;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private Set<Report> reports;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private Set<Contact> contacts;

    @OneToMany(mappedBy = "shipper_account")
    @JsonIgnore
    private Set<Order> ordersByShipper;

    @OneToMany(mappedBy = "staff_manager_account")
    @JsonIgnore
    private Set<Order> ordersByStaffManager;

    @OneToMany(mappedBy = "customer_account")
    @JsonIgnore
    private Set<Order> ordersByCustomer;

    @OneToMany(mappedBy = "manager_account")
    @JsonIgnore
    private Set<Ingredient> ingredients;

    @OneToMany(mappedBy = "manager_account")
    @JsonIgnore
    private Set<Staff> mStaffs;

    @OneToMany(mappedBy = "deliver_account")
    @JsonIgnore
    private Set<Staff> mShipperStaffs;

    public boolean isOTPRequired() {
        if (this.getOneTimePassword() == null) {
            return false;
        }
        long currentTimeInMillis = System.currentTimeMillis();
        long otpRequestedTimeInMillis = this.otpRequestedTime.getTime();
        if ((otpRequestedTimeInMillis + OTP_VALID_DURATION) <= currentTimeInMillis) {
            //OTP expired
            return false;
        }
        return true;
    }

}