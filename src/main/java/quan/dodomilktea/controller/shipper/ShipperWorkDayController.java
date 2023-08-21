package quan.dodomilktea.controller.shipper;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quan.dodomilktea.dto.shipper.WorkDayDto;
import quan.dodomilktea.service.WorkDayService;

@RestController
@RequestMapping("/shipper/workday")
@CrossOrigin(origins ="http://localhost:3000")
public class ShipperWorkDayController {

    @Autowired
    WorkDayService workDayService;

    @GetMapping("get-workday")
    public ResponseEntity<Object> getWorkDay(HttpServletRequest request) {
        List<WorkDayDto> workDayDtos = workDayService.getWorkDay(request);

        if (workDayDtos == null) {
            throw new NullPointerException();
        }

        return ResponseEntity.ok(workDayDtos);
    }
}
