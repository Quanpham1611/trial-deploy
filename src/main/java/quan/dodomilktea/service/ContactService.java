package quan.dodomilktea.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quan.dodomilktea.dto.admin.ContactRequestDto;
import quan.dodomilktea.model.Contact;
import quan.dodomilktea.repo.AccountRepository;
import quan.dodomilktea.repo.ContactRepository;

import java.util.List;
@Service
public class ContactService {
    @Autowired
    ContactRepository contactRepo;
    @Autowired
    AccountRepository accountRepository;
    Logger logger = LoggerFactory.getLogger(ContactService.class);

    public List<Contact> getAllContact() {
        return contactRepo.findAll();
    }

    public Contact insertContact(ContactRequestDto contactInsert) {
        Contact contactResponse = new Contact();
        contactResponse.setType(contactInsert.getType());
        contactResponse.setContent(contactInsert.getContent());
        contactResponse.setAccount(accountRepository.findById(contactInsert.getAccountId()).orElse(null));
        contactResponse.setEnabled(true);

        try {
            contactResponse = contactRepo.save(contactResponse);
        } catch(Exception e) {
            logger.error(e.getMessage());
            contactResponse = null;
        }
        return contactResponse;
    }

    public Contact updateContact(ContactRequestDto contactUpdate, String id) {
        Contact contactResponse = contactRepo.findById(id).orElse(null);
        if(contactResponse == null || contactResponse.isEnabled() == false) { return null; }

        contactResponse.setType(contactUpdate.getType());
        contactResponse.setContent(contactUpdate.getContent());
        contactResponse.setAccount(accountRepository.findById(contactUpdate.getAccountId()).orElse(null));

        try {
            contactResponse = contactRepo.save(contactResponse);
        } catch(Exception e) {
            logger.error(e.getMessage());
            contactResponse = null;
        }
        return contactResponse;
    }

    public boolean deleteContact(String id) {
        Contact contactDelete = contactRepo.findById(id).orElse(null);
        if(contactDelete == null || contactDelete.isEnabled() == false) { return false; }

        contactDelete.setEnabled(false);
        try {
            contactRepo.save(contactDelete);
        } catch(Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }
}