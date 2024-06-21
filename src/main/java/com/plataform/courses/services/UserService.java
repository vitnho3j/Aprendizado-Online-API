package com.plataform.courses.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plataform.courses.model.entity.User;
import com.plataform.courses.repository.UserRepository;
import com.plataform.courses.services.exceptions.BadWordException;
import com.plataform.courses.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    private static String UNTANTED_CONTENT = "Conteúdo indesejado detectado";

    public User findById(Long id){
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(()-> new ObjectNotFoundException(
            "Usuário não encontrado! Id: " + id + ", Tipo: " + User.class.getName()
        ));
    }

    @Transactional
    public User create(User obj){
        ContentFilterService filterService = new ContentFilterService();
        List<String> fieldsToCheck = Arrays.asList(obj.getName(), obj.getEmail());
        if (filterService.containsBadWord(fieldsToCheck)) {
            throw new BadWordException(
                UNTANTED_CONTENT
            );
        }
        obj.setId(null);
        obj = this.userRepository.save(obj);
        return obj;
    }

    @Transactional
    public User update(User obj){
        ContentFilterService filterService = new ContentFilterService();
        List<String> fieldsToCheck = Arrays.asList(obj.getName(), obj.getEmail());
        if (filterService.containsBadWord(fieldsToCheck)) {
            throw new BadWordException(UNTANTED_CONTENT);
        }
        User newObj = findById(obj.getId());
        newObj.setName(obj.getName());
        newObj.setEmail(obj.getEmail());
        return this.userRepository.save(newObj);
    }

    public void delete(Long id){
        findById(id);
        try{
            this.userRepository.deleteById(id);
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<User> getAll() {
        List<User> courses = this.userRepository.findAll();
        return courses;
    }

}
