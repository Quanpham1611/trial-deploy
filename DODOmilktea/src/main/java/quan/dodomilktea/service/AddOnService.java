package quan.dodomilktea.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quan.dodomilktea.model.AddOn;
import quan.dodomilktea.repo.AddOnRepository;

import java.util.List;
@Service
public class AddOnService {
    @Autowired
    AddOnRepository addOnRepository;

    public AddOn getAddOnById(String id) {
        return addOnRepository.findById(id).orElseThrow(() -> new NullPointerException());
    }

    public List<AddOn> getAllAddOns() {
        return addOnRepository.findAll();
    }
}
