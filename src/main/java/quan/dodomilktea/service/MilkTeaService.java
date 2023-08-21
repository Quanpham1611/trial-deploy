package quan.dodomilktea.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quan.dodomilktea.dto.admin.MilkteaRequestDto;
import quan.dodomilktea.model.MilkTea;
import quan.dodomilktea.model.MilkTeaCategory;
import quan.dodomilktea.repo.MilkTeaCategoryRepository;
import quan.dodomilktea.repo.MilkTeaRepository;

import java.util.List;

@Service
public class MilkTeaService {

    @Autowired
    MilkTeaRepository milkTeaRepo;
    @Autowired
    MilkTeaCategoryRepository milkTeaCategoryRepository;
    Logger logger = LoggerFactory.getLogger(MilkTeaService.class);

    public List<MilkTea> searchMilkTeas(String keyword) {
        if (keyword == null) {
            return getAllMilkTea();
        }
        keyword = keyword.replace("+", " ");
        return milkTeaRepo.findMilkTeaByName(keyword);
    }

    public List<MilkTea> getAllMilkTea() {
        return milkTeaRepo.findAll();
    }

    public MilkTea getMilkTeaById(String id) {
        return milkTeaRepo.findById(id).orElseThrow(() -> new NullPointerException());
    }

    public MilkTea insertMilktea(MilkteaRequestDto milkTeaInsert) {
        // Check name is exist or not ?
        List<MilkTea> milkTeas = getAllMilkTea();
        for (MilkTea milkTea : milkTeas) {
            if(milkTea.getName().equalsIgnoreCase(milkTeaInsert.getName())) {
                return null;
            }
        }

        MilkTea milkTeaResponse = new MilkTea();
        milkTeaResponse.setName(milkTeaInsert.getName());
        milkTeaResponse.setPrice(milkTeaInsert.getPrice());
        milkTeaResponse.setCost(milkTeaInsert.getCost());
        milkTeaResponse.setImage_url(milkTeaInsert.getImageUrl());
        milkTeaResponse.setEnabled(true);

        MilkTeaCategory category = milkTeaCategoryRepository.findById(milkTeaInsert.getCategoryId()).orElse(null);
        if(category == null) {
            return null;
        }
        milkTeaResponse.setMilk_tea_category(category);

        try {
            milkTeaResponse = milkTeaRepo.save(milkTeaResponse);
        } catch(Exception e) {
            logger.error(e.getMessage());
            milkTeaResponse = null;
        }
        return milkTeaResponse;
    }

    public MilkTea updateMilkTea(MilkteaRequestDto milkTeaUpdate, String id) {
        MilkTea milkTeaResponse = milkTeaRepo.findById(id).orElse(null);
        if(milkTeaResponse == null || milkTeaResponse.isEnabled() == false) { return null; }

        // Check name is exist or not ?
        List<MilkTea> milkTeas = getAllMilkTea();
        for (MilkTea milkTea : milkTeas) {
            if(milkTea.getName().equalsIgnoreCase(milkTeaUpdate.getName())) {
                if(milkTea.getName().equalsIgnoreCase(milkTeaResponse.getName()))
                    continue;
                return null;
            }
        }

        milkTeaResponse.setName(milkTeaUpdate.getName());
        milkTeaResponse.setPrice(milkTeaUpdate.getPrice());
        milkTeaResponse.setCost(milkTeaUpdate.getCost());
        milkTeaResponse.setImage_url(milkTeaUpdate.getImageUrl());

        MilkTeaCategory category = milkTeaCategoryRepository.findById(milkTeaUpdate.getCategoryId()).orElse(null);
        if(category == null) {
            return null;
        }
        milkTeaResponse.setMilk_tea_category(category);

        try {
            milkTeaResponse = milkTeaRepo.save(milkTeaResponse);
        } catch(Exception e) {
            logger.error(e.getMessage());
            milkTeaResponse = null;
        }
        return milkTeaResponse;
    }

    public boolean deleteMilkTea(String id) {
        MilkTea milkteaDelete = milkTeaRepo.findById(id).orElse(null);
        if(milkteaDelete == null || milkteaDelete.isEnabled() == false) { return false; }

        milkteaDelete.setEnabled(false);
        try {
            milkTeaRepo.save(milkteaDelete);
        } catch(Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }
}