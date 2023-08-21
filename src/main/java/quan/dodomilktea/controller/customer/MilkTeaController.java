package quan.dodomilktea.controller.customer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quan.dodomilktea.dto.customer.MilkTeaDetailDto;
import quan.dodomilktea.model.AddOn;
import quan.dodomilktea.model.MilkTea;
import quan.dodomilktea.service.AddOnService;
import quan.dodomilktea.service.MilkTeaService;

@RestController
@RequestMapping("/home")
@CrossOrigin(origins ="http://localhost:3000")
public class MilkTeaController {
    @Autowired
    MilkTeaService milkTeaService;
    @Autowired
    AddOnService addOnService;

    @GetMapping("/search/{keyword}")
    public ResponseEntity<?> listMilkTeaRelate(@PathVariable String keyword) {
        List<MilkTea> allMilkTeaRelate = new ArrayList<>();
        allMilkTeaRelate = milkTeaService.searchMilkTeas(keyword);
        return ResponseEntity.ok(allMilkTeaRelate);
    }

    @GetMapping("/all-milk-teas")
    public ResponseEntity<?> listMTeas() {
        return ResponseEntity.ok(milkTeaService.getAllMilkTea());
    }

    @GetMapping("/get-milk-tea/{mteaid}")
    public ResponseEntity<?> getSpecificMilkTea(@PathVariable String mteaid) {
        MilkTea milkTea = milkTeaService.getMilkTeaById(mteaid);
        List<AddOn> allAddOns = addOnService.getAllAddOns();
        MilkTeaDetailDto milkTeaDetailDto = new MilkTeaDetailDto(milkTea, allAddOns);
        return ResponseEntity.ok(milkTeaDetailDto);
    }
}
