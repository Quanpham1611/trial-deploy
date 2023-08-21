package quan.dodomilktea.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quan.dodomilktea.model.MilkTeaCategory;
import quan.dodomilktea.repo.MilkTeaCategoryRepository;

import java.util.List;

@Service
public class MilkTeaCategoryService {

    @Autowired
    private MilkTeaCategoryRepository milkTeaCategoryRepository;
    Logger logger = LoggerFactory.getLogger(MilkTeaCategoryService.class);

    public List<MilkTeaCategory> getAllCategories() {
        return milkTeaCategoryRepository.findAll();
    }

    public MilkTeaCategory insertCategory(MilkTeaCategory milkTeaCategoryInsert) {
        // Check name is exist or not ?
        List<MilkTeaCategory> milkTeaCategories = getAllCategories();
        for (MilkTeaCategory milkTeaCategory : milkTeaCategories) {
            if(milkTeaCategory.getCategory_name().equalsIgnoreCase(milkTeaCategoryInsert.getCategory_name())) {
                return null;
            }
        }

        milkTeaCategoryInsert.setEnabled(true);

        try {
            milkTeaCategoryInsert = milkTeaCategoryRepository.save(milkTeaCategoryInsert);
        } catch(Exception e) {
            logger.error(e.getMessage());
            milkTeaCategoryInsert = null;
        }
        return milkTeaCategoryInsert;
    }

    public MilkTeaCategory updateMilkTeaCategory(MilkTeaCategory milkTeaCategoryUpdate, String id) {
        MilkTeaCategory milkTeaCategoryResponse = milkTeaCategoryRepository.findById(id).orElse(null);
        if(milkTeaCategoryResponse == null || milkTeaCategoryResponse.isEnabled() == false) { return null; }

        // Check name is exist or not ?
        List<MilkTeaCategory> milkTeaCategories = getAllCategories();
        for (MilkTeaCategory milkTeaCategory : milkTeaCategories) {
            if(milkTeaCategory.getCategory_name().equalsIgnoreCase(milkTeaCategoryUpdate.getCategory_name())) {
                if(milkTeaCategory.getCategory_name().equalsIgnoreCase(milkTeaCategoryResponse.getCategory_name()))
                    continue;
                return null;
            }
        }

        milkTeaCategoryResponse.setCategory_name(milkTeaCategoryUpdate.getCategory_name());
        milkTeaCategoryResponse.setDescription(milkTeaCategoryUpdate.getDescription());


        try {
            milkTeaCategoryResponse = milkTeaCategoryRepository.save(milkTeaCategoryResponse);
        } catch(Exception e) {
            logger.error(e.getMessage());
            milkTeaCategoryResponse = null;
        }
        return milkTeaCategoryResponse;
    }

    public boolean deleteMilkTeaCategory(String id) {
        MilkTeaCategory milkteaCategoryDelete = milkTeaCategoryRepository.findById(id).orElse(null);
        if(milkteaCategoryDelete == null || milkteaCategoryDelete.isEnabled() == false) { return false; }

        milkteaCategoryDelete.setEnabled(false);
        try {
            milkTeaCategoryRepository.save(milkteaCategoryDelete);
        } catch(Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }
}
