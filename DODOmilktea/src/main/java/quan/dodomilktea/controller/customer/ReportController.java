package quan.dodomilktea.controller.customer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quan.dodomilktea.dto.customer.MessageDto;
import quan.dodomilktea.model.Report;
import quan.dodomilktea.service.ReportService;

@RestController
@RequestMapping("/home")
@CrossOrigin(origins ="http://localhost:3000")
public class ReportController {
    @Autowired
    ReportService reportService;

    @PostMapping("/report")
    public ResponseEntity<?> sendReport(@RequestBody @Valid Report report, HttpServletRequest request) {
        reportService.createReport(report, request);
        return ResponseEntity.ok(new MessageDto("Report sent successfully!!!"));
    }
}
