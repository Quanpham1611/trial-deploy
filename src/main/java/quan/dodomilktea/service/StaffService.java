package quan.dodomilktea.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quan.dodomilktea.dto.admin.StaffRequestDto;
import quan.dodomilktea.model.Staff;
import quan.dodomilktea.repo.AccountRepository;
import quan.dodomilktea.repo.SalaryRepository;
import quan.dodomilktea.repo.StaffRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class StaffService {
    @Autowired
    StaffRepository staffRepository;

    @Autowired
    SalaryRepository salaryRepository;

    @Autowired
    AccountRepository accountRepository;

    Logger logger = LoggerFactory.getLogger(StaffService.class);

    public List<Staff> getStaffList(String role) {
        List<Staff> staffList = new ArrayList<>();

        if(role.equalsIgnoreCase("All")) {
            staffList = staffRepository.findAll();
        } else {
            staffList = staffRepository.findByRole(role);
        }
        return staffList;
    }

    public Staff insertStaff(StaffRequestDto staffInsert) {
        Staff staffResponse = staffRepository.findById(staffInsert.getId()).orElse(null);
        if(staffResponse != null) {
            return null;
        }
        staffResponse = new Staff();
        staffResponse = setData(staffResponse, staffInsert);
        staffResponse.setStart_date(LocalDate.now());

        try {
            staffResponse = staffRepository.save(staffResponse);
        } catch(Exception e) {
            logger.error(e.getMessage());
            staffResponse = null;
        }
        return staffResponse;
    }

    public Staff updateStaff(StaffRequestDto staffUpdate, String id) {
        Staff staffResponse = staffRepository.findById(id).orElse(null);
        if(staffResponse == null || staffResponse.isEnabled() == false) { return null; }

        staffResponse = setData(staffResponse, staffUpdate);
        staffResponse.setId_number(id);

        try {
            staffResponse = staffRepository.save(staffResponse);
        } catch(Exception e) {
            logger.error(e.getMessage());
            staffResponse = null;
        }
        return staffResponse;
    }

    private Staff setData(Staff staffResponse, StaffRequestDto staffInsert) {
        staffResponse.setId_number(staffInsert.getId());
        staffResponse.setName(staffInsert.getName());
        staffResponse.setPhone(staffInsert.getPhone());
        staffResponse.setEmail(staffInsert.getEmail());
        staffResponse.setAddress(staffInsert.getAddress());
        staffResponse.setStaff_role(staffInsert.getRole());

        LocalDate date = LocalDate.parse(staffInsert.getDateOfBirth());
        staffResponse.setDay_of_birth(date);
        staffResponse.setSalary(salaryRepository.findSalaryByRole(staffInsert.getRole(), false));
        staffResponse.setOt_salary(salaryRepository.findSalaryByRole(staffInsert.getRole(), true));
        staffResponse.setManager(staffRepository.findById(staffInsert.getManagerId()).orElse(null));
        staffResponse.setManager_account(accountRepository.findById(staffInsert.getManagerAccount()).orElse(null));
        staffResponse.setDeliver_account(accountRepository.findById(staffInsert.getDeliverAccount()).orElse(null));
        staffResponse.setEnabled(true);
        return staffResponse;
    }

    public boolean deleteStaff(String id) {
        Staff staffDelete = staffRepository.findById(id).orElse(null);
        if(staffDelete == null || staffDelete.isEnabled() == false) { return false; }

        staffDelete.setEnabled(false);
        try {
            staffRepository.save(staffDelete);
        } catch(Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }
}
