package quan.dodomilktea.controller.staffmamager;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quan.dodomilktea.model.AddOn;
import quan.dodomilktea.model.CustomMilkTea;
import quan.dodomilktea.service.AddOnService;
import quan.dodomilktea.service.CustomMilkTeaService;
import quan.dodomilktea.service.MilkTeaService;

@RestController
@RequestMapping("/staff-manager")
@CrossOrigin(origins ="http://localhost:3000")
public class SMOfflineOrderController {
    @Autowired
    MilkTeaService milkTeaService;
    @Autowired
    AddOnService addOnService;
    @Autowired
    CustomMilkTeaService customMilkTeaService;


    @GetMapping("/all-milk-teas")
    public ResponseEntity<?> listMTeas() {
        return ResponseEntity.ok(milkTeaService.getAllMilkTea());
    }

    @GetMapping("/get-milk-tea/{mteaid}")
    public ResponseEntity<?> getSpecificMilkTea(@PathVariable String mteaid) {
        return ResponseEntity.ok(milkTeaService.getMilkTeaById(mteaid));
    }

    @PostMapping(value = { "/create-offline-order/{mteaid}/{addOnIds}", "/create-offline-order/{mteaid}" })
    public ResponseEntity<?> createOfflineOrder(@RequestBody @Valid CustomMilkTea customMilkTea,
                                                @PathVariable String mteaid, @PathVariable @Nullable String addOnIds) {
        // If have addons in url
        if (addOnIds != null) {
            customMilkTea = saveAddOnIntoCMT(addOnIds, customMilkTea);
        }

        customMilkTea.setMilkTea(milkTeaService.getMilkTeaById(mteaid));

        // Check custom milk tea existed or not, if not create a new one
        customMilkTea.setTotal_price(getTotalPrice(customMilkTea));
        customMilkTea.setTotal_cost(getTotalCost(customMilkTea));
        CustomMilkTea savedCustomMilkTea = customMilkTeaService.saveCustomMilkTea(customMilkTea);

        return ResponseEntity.ok(savedCustomMilkTea);
    }

    // calculate totalPrice of custom milk tea
    private int getTotalPrice(@Valid CustomMilkTea customMilkTea) {
        int totalPrice = 0;
        if (customMilkTea.getAddOn() != null) {
            for (AddOn addOn : customMilkTea.getAddOn()) {
                totalPrice += addOn.getPrice();
            }
        }
        totalPrice += customMilkTea.getMilkTea().getPrice();
        return totalPrice;
    }

    // calculate totalCost of custom milktea
    private int getTotalCost(@Valid CustomMilkTea customMilkTea) {
        int totalCost = 0;
        if (customMilkTea.getAddOn() != null) {
            for (AddOn addOn : customMilkTea.getAddOn()) {
                totalCost += addOn.getCost();
            }
        }
        totalCost += customMilkTea.getMilkTea().getCost();
        return totalCost;
    }

    // Save add on into custom milk tea
    private @Valid CustomMilkTea saveAddOnIntoCMT(String addOnIds, @Valid CustomMilkTea customMilkTea) {
        List<AddOn> addOns = new ArrayList<>();
        String[] addOnsIdArr;

        if (addOnIds.contains("+"))
            addOnsIdArr = addOnIds.split("\\+");
        else
            addOnsIdArr = new String[] { addOnIds };

        if (addOnsIdArr.length > 0) {
            for (String addOnId : addOnsIdArr) {
                addOns.add(addOnService.getAddOnById(addOnId));
            }
        }
        customMilkTea.setAddOn(addOns);
        return customMilkTea;
    }
}
