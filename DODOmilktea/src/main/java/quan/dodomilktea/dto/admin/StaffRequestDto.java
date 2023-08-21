package quan.dodomilktea.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffRequestDto {
    private String id;
    private String name;
    private String phone;
    private String address;
    private String email;
    private String role;
    private String dateOfBirth;
    private String managerId;
    private String managerAccount;
    private String deliverAccount;
}