package quan.dodomilktea.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quan.dodomilktea.dto.customer.MessageDto;
import quan.dodomilktea.model.Report;
import quan.dodomilktea.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/admin/report")
@CrossOrigin(origins ="http://localhost:3000")
public class AdminReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        if (reports == null) {
            throw new NullPointerException();
        }
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/get-report")
    public ResponseEntity<Object> getReportDetail(@RequestParam String reportId) {
        Report report = reportService.getReportDetail(reportId);
        if (report == null) {
            throw new NullPointerException();
        }
        return ResponseEntity.ok(report);
    }

    @PutMapping("/delete/{reportId}")
    public ResponseEntity<Object> deleteReport(@PathVariable String reportId) {
        boolean isDeleted = reportService.deleteReport(reportId);
        if (isDeleted) {
            return ResponseEntity.ok(new MessageDto("Delete Report Successfully"));
        }
        return ResponseEntity.ok(new MessageDto("Delete Report Unsuccessfully, please check again !"));
    }

    @PutMapping("/have-read/{reportId}")
    public ResponseEntity<Object> haveReadReport(@PathVariable String reportId) {
        boolean isRead = reportService.haveReadReport(reportId);
        if (isRead) {
            return ResponseEntity.ok(new MessageDto("Update Report State Successfully"));
        }
        return ResponseEntity.ok(new MessageDto("Update Report State Unsuccessfully, please check again !"));
    }
}
