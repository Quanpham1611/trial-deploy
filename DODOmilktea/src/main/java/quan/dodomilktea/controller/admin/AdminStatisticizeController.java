package quan.dodomilktea.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quan.dodomilktea.dto.admin.StatisticizeDto;
import quan.dodomilktea.service.OrderService;


@RestController
@RequestMapping("/admin/statisticize")
@CrossOrigin(origins ="http://localhost:3000")
public class AdminStatisticizeController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/get-by-date")
    public ResponseEntity<Object> getByDate(@RequestParam String date) {
        StatisticizeDto result = orderService.statisticizeByDate(date);
        if(result == null) {
            throw new NullPointerException();
        }
        return ResponseEntity.ok(result);
    }

}
