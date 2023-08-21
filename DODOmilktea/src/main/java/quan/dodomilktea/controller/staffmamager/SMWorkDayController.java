package quan.dodomilktea.controller.staffmamager;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quan.dodomilktea.service.WorkDayService;

@RestController
@RequestMapping("/staff-manager")
@CrossOrigin(origins ="http://localhost:3000")
public class SMWorkDayController {
    @Autowired
    WorkDayService workDayService;
    @GetMapping("/work-day")
    public ResponseEntity<?> getTodayWorkDay() {
        LocalDate date = LocalDate.now();
        return ResponseEntity.ok(workDayService.getWorkDay(date));
    }

    @GetMapping("/work-day/{date}")
    public ResponseEntity<?> getWorkDayByDate(@PathVariable @Valid String date) {
        LocalDate dateIn = LocalDate.parse(date);
        return ResponseEntity.ok(workDayService.getWorkDay(dateIn));
    }

    @PutMapping("/work-day/update/{staffId}/{workDay}/{hour}")
    public ResponseEntity<?> changeWorkHour(@PathVariable String staffId, @PathVariable String workDay,
                                            @PathVariable float hour) {
        LocalDate dateIn = LocalDate.parse(workDay);
        return ResponseEntity.ok(workDayService.updateWorkHour(staffId, dateIn, hour));
    }
}
