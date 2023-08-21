package quan.dodomilktea.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quan.dodomilktea.dto.admin.MilkteaRequestDto;
import quan.dodomilktea.dto.customer.MessageDto;
import quan.dodomilktea.model.MilkTea;
import quan.dodomilktea.service.MilkTeaService;

import java.util.List;

@RestController
@RequestMapping("/admin/milktea")
@CrossOrigin(origins ="http://localhost:3000")
public class AdminMilkteaController {

    @Autowired
    private MilkTeaService milkTeaService;

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAllMilkteas() {
        List<MilkTea> milkteas = milkTeaService.getAllMilkTea();
        if (milkteas == null) {
            throw new NullPointerException();
        }
        return ResponseEntity.ok(milkteas);
    }

    @PostMapping("/insert")
    public ResponseEntity<Object> insertMilktea(@RequestBody MilkteaRequestDto milkteaInsert) {
        MilkTea milkteaResponse = milkTeaService.insertMilktea(milkteaInsert);
        if (milkteaResponse == null) {
            return ResponseEntity.ok(new MessageDto("Insert Milktea Unsuccessfully, please check again !"));
        }
        return ResponseEntity.ok(milkteaResponse);
    }

    @PutMapping("/update/{milkTeaId}")
    public ResponseEntity<Object> updateMilktea(@RequestBody MilkteaRequestDto milkteUpdate, @PathVariable String milkTeaId) {
        MilkTea milkteaResponse = milkTeaService.updateMilkTea(milkteUpdate, milkTeaId);
        if (milkteaResponse == null) {
            return ResponseEntity.ok(new MessageDto("Update Milktea Unsuccessfully, please check again !"));
        }
        return ResponseEntity.ok(milkteaResponse);
    }

    @PutMapping("/delete/{milkTeaId}")
    public ResponseEntity<Object> deleteMilkTea(@PathVariable String milkTeaId) {
        boolean isDeleted = milkTeaService.deleteMilkTea(milkTeaId);
        if (isDeleted) {
            return ResponseEntity.ok(new MessageDto("Delete Milktea Successfully"));
        }
        return ResponseEntity.ok(new MessageDto("Delete Milktea Unsuccessfully, please check again !"));
    }
}
