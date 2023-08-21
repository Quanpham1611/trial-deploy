package quan.dodomilktea.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quan.dodomilktea.jwt.JwtTokenUlti;
import quan.dodomilktea.model.Account;
import quan.dodomilktea.model.Report;
import quan.dodomilktea.repo.AccountRepository;
import quan.dodomilktea.repo.ReportRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepo;
    @Autowired
    JwtTokenUlti jwtTokenUtil;
    @Autowired
    AccountRepository accountRepo;
    Logger logger = LoggerFactory.getLogger(ReportService.class);

    public void createReport(Report report, HttpServletRequest request) {
        report.setSend_date(new Date());
        //Check token with acc_id
        if (jwtTokenUtil.hasAuthorizationHeader(request)) {
            String token = jwtTokenUtil.getAccessToken(request);
            if(jwtTokenUtil.validateAccessToken(token)) {
                String[] subjectArray = jwtTokenUtil.getSubject(token).split(",");
                Account account = accountRepo.findById(subjectArray[0]).get();
                if (account != null) {
                    report.setAccount(account);
                }
            }
        }
        report.setHas_read(false);
        report.setEnabled(true);
        reportRepo.save(report);
    }

    public List<Report> getAllReports() {
        return reportRepo.findAll();
    }

    public Report getReportDetail(String id) {
        Report report = reportRepo.findById(id).orElse(null);
        return report;
    }

    public boolean deleteReport(String id) {
        Report report = reportRepo.findById(id).orElse(null);
        if(report == null || report.isEnabled() == false) { return false; }

        report.setEnabled(false);
        try {
            reportRepo.save(report);
        } catch(Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean haveReadReport(String id) {
        Report report = reportRepo.findById(id).orElse(null);
        if(report == null || report.isEnabled() == false) { return false; }

        report.setHas_read(true);
        try {
            reportRepo.save(report);
        } catch(Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }
}
