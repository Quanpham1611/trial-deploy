package quan.dodomilktea.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quan.dodomilktea.dto.admin.IngredientRequestDto;
import quan.dodomilktea.jwt.JwtTokenUlti;
import quan.dodomilktea.model.Account;
import quan.dodomilktea.model.Ingredient;
import quan.dodomilktea.repo.IngredientRepository;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class IngredientService {
    @Autowired
    IngredientRepository ingredientRepo;
    @Autowired
    JwtTokenUlti jwtTokenUtil;
    @Autowired AccountService accountService;
    Logger logger = LoggerFactory.getLogger(IngredientService.class);


    public List<Ingredient> getIngredientsManage(HttpServletRequest request) {
        String token = jwtTokenUtil.getAccessToken(request);
        String[] subjectArray = jwtTokenUtil.getSubject(token).split(",");
        Account account = accountService.getAccById(subjectArray[0]);
        return ingredientRepo.findAllManage(account.getAcc_id());
    }

    public Ingredient getIngredientById(String id) {
        return ingredientRepo.findById(id).orElseThrow(() -> new NullPointerException());
    }

    public Ingredient updateIngredient(Ingredient ingredient) {
        return ingredientRepo.save(ingredient);
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientRepo.findAll();
    }

    public Ingredient insertIngredient(IngredientRequestDto ingredientInsert, HttpServletRequest request) {
        Ingredient ingredientResponse = setData(ingredientInsert, request);

        if (ingredientResponse == null) { return null; }

        try {
            ingredientResponse = ingredientRepo.save(ingredientResponse);
        } catch(Exception e) {
            logger.error(e.getMessage());
            ingredientResponse = null;
        }
        return ingredientResponse;
    }

    public Ingredient updateIngredient(IngredientRequestDto ingredientUpdate, String id, HttpServletRequest request) {
        Ingredient ingredientResponse = ingredientRepo.findById(id).orElse(null);
        if(ingredientResponse == null || ingredientResponse.isEnabled() == false) { return null; }
        ingredientResponse = setData(ingredientUpdate, request);
        ingredientResponse.setIngredient_id(id);

        try {
            ingredientResponse = ingredientRepo.save(ingredientResponse);
        } catch(Exception e) {
            logger.error(e.getMessage());
            ingredientResponse = null;
        }
        return ingredientResponse;
    }

    public boolean deleteIngredient(String id) {
        Ingredient ingredientDelete = ingredientRepo.findById(id).orElse(null);
        if(ingredientDelete == null || ingredientDelete.isEnabled() == false) { return false; }

        ingredientDelete.setEnabled(false);
        try {
            ingredientRepo.save(ingredientDelete);
        } catch(Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    private Ingredient setData(IngredientRequestDto ingredientInsert, HttpServletRequest request) {
        Ingredient ingredientResponse = new Ingredient();
        ingredientResponse.setName(ingredientInsert.getName());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            ingredientResponse.setManufactoring_date(formatter.parse(ingredientInsert.getManufactoringDate()));
            ingredientResponse.setExpired_date(formatter.parse(ingredientInsert.getExpiredDate()));
        } catch (Exception e) {
            return null;
        }

        ingredientResponse.setCost(ingredientInsert.getCost());
        ingredientResponse.setQuantity(ingredientInsert.getQuantity());
        ingredientResponse.setLast_update(new Date());
        ingredientResponse.setNote(ingredientInsert.getNote());
        ingredientResponse.setEnabled(true);

        String token = jwtTokenUtil.getAccessToken(request);
        String[] subjectArray = jwtTokenUtil.getSubject(token).split(",");
        Account account = accountService.getAccById(subjectArray[0]);
        ingredientResponse.setManager_account(account);
        return ingredientResponse;
    }
}
