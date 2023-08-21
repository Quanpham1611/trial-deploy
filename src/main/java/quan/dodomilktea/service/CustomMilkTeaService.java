package quan.dodomilktea.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quan.dodomilktea.model.CustomMilkTea;
import quan.dodomilktea.repo.CustomMilkTeaRepository;

import java.util.List;

@Service
public class CustomMilkTeaService {
    @Autowired
    CustomMilkTeaRepository customMilkTeaRepository;

    public CustomMilkTea getCustomMilkTeaById( String id) {
        return customMilkTeaRepository.findById(id).orElseThrow(() -> new NullPointerException());
    }

    public CustomMilkTea saveCustomMilkTea(CustomMilkTea customMilkTea) {
        customMilkTea.setEnabled(true);
        return customMilkTeaRepository.save(customMilkTea);
    }

    public List<CustomMilkTea> getCustomMilkTeaInCart(String cartId) {
        return customMilkTeaRepository.findByCartId(cartId);
    }

    public String deleteCustomMilkTea(String cmtId) {
        CustomMilkTea customMilkTea = getCustomMilkTeaById(cmtId);
        customMilkTeaRepository.delete(customMilkTea);
        return "Delete successfully!!";
    }

    public String updateCustomMilkTea(CustomMilkTea customMilkTea, String cmtId) {
        CustomMilkTea updateCustomMilkTea = getCustomMilkTeaById(cmtId);
        updateCustomMilkTea.setSize(customMilkTea.getSize());
        updateCustomMilkTea.setIce_amount(customMilkTea.getIce_amount());
        updateCustomMilkTea.setSugar_amount(customMilkTea.getSugar_amount());
        customMilkTeaRepository.save(updateCustomMilkTea);
        return "Update successfully";
    }

    public List<CustomMilkTea> getCustomMilkteaByOrder(String orderId) {
        return customMilkTeaRepository.findByOrderId(orderId);
    }
}
