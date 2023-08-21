package quan.dodomilktea.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quan.dodomilktea.dto.admin.StaffRequestDto;
import quan.dodomilktea.dto.customer.MessageDto;
import quan.dodomilktea.model.Staff;
import quan.dodomilktea.service.StaffService;

import java.util.List;

@RestController
@RequestMapping("/admin/staff")
@CrossOrigin(origins ="http://localhost:3000")
public class AdminStaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping("/get-staff-list/{role}")
    public ResponseEntity<Object> getStaffList(@PathVariable String role) {
        List<Staff> staffs = staffService.getStaffList(role);
        if (staffs == null) {
            throw new NullPointerException();
        }
        return ResponseEntity.ok(staffs);
    }

    @PostMapping("/insert")
    public ResponseEntity<Object> insertStaff(@RequestBody StaffRequestDto staffInsert) {
        Staff staffResponse = staffService.insertStaff(staffInsert);
        if (staffResponse == null) {
            return ResponseEntity.ok(new MessageDto("Insert Staff Unsuccessfully, duplicate Email, Id or Phone Number !"));
        }
        return ResponseEntity.ok(staffResponse);
    }


    @PutMapping("/update/{staffId}")
    public ResponseEntity<Object> updateStaff(@RequestBody StaffRequestDto staffUpdate, @PathVariable String staffId) {
        Staff staffResponse = staffService.updateStaff(staffUpdate, staffId);
        if (staffResponse == null) {
            return ResponseEntity.ok(new MessageDto("Update Staff Unsuccessfully, not found staff or duplicate data, please check again !"));
        }
        return ResponseEntity.ok(staffResponse);
    }

    @PutMapping("/delete/{staffId}")
    public ResponseEntity<Object> deleteStaff(@PathVariable String staffId) {
        boolean isDeleted = staffService.deleteStaff(staffId);
        if (isDeleted) {
            return ResponseEntity.ok(new MessageDto("Delete Staff Successfully"));
        }
        return ResponseEntity.ok(new MessageDto("Delete Staff Unsuccessfully, please check again !"));
    }
}
