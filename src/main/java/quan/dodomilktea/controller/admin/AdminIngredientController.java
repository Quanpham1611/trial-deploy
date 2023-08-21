package quan.dodomilktea.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quan.dodomilktea.dto.admin.IngredientRequestDto;
import quan.dodomilktea.dto.customer.MessageDto;
import quan.dodomilktea.model.Ingredient;
import quan.dodomilktea.service.IngredientService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/admin/ingredient")
@CrossOrigin(origins ="http://localhost:3000")
public class AdminIngredientController {
    @Autowired
    private IngredientService ingredientService;

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAllIngredients() {
        List<Ingredient> ingredients = ingredientService.getAllIngredients();
        if (ingredients == null) {
            throw new NullPointerException();
        }
        return ResponseEntity.ok(ingredients);
    }

    @PostMapping("/insert")
    public ResponseEntity<Object> insertIngredient(@RequestBody IngredientRequestDto milkteaInsert, HttpServletRequest request) {
        Ingredient ingredientResponse = ingredientService.insertIngredient(milkteaInsert, request);
        if (ingredientResponse == null) {
            return ResponseEntity.ok(new MessageDto("Insert Ingredient Unsuccessfully, please check again !"));
        }
        return ResponseEntity.ok(ingredientResponse);
    }

    @PutMapping("/update/{ingredientId}")
    public ResponseEntity<Object> updateIngredient(@RequestBody IngredientRequestDto ingredientUpdate, @PathVariable String ingredientId,
                                                   HttpServletRequest request) {
        Ingredient ingredientResponse = ingredientService.updateIngredient(ingredientUpdate, ingredientId, request);
        if (ingredientResponse == null) {
            return ResponseEntity.ok(new MessageDto("Update Ingredient Unsuccessfully, please check again !"));
        }
        return ResponseEntity.ok(ingredientResponse);
    }

    @PutMapping("/delete/{ingredientId}")
    public ResponseEntity<Object> deleteIngredient(@PathVariable String ingredientId) {
        boolean isDeleted = ingredientService.deleteIngredient(ingredientId);
        if (isDeleted) {
            return ResponseEntity.ok(new MessageDto("Delete Ingredient Successfully"));
        }
        return ResponseEntity.ok(new MessageDto("Delete Ingredient Unsuccessfully, please check again !"));
    }
}
