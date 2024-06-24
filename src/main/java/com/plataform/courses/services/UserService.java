package com.plataform.courses.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plataform.courses.model.dto.UserCreateDTO;
import com.plataform.courses.model.dto.UserUpdateDTO;
import com.plataform.courses.model.entity.User;
import com.plataform.courses.repository.UserRepository;
import com.plataform.courses.services.exceptions.BadWordException;
import com.plataform.courses.services.exceptions.NotPermissionImmutableData;
import com.plataform.courses.services.exceptions.ObjectNotFoundException;

import jakarta.validation.Valid;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    private static String UNTANTED_CONTENT = "Conteúdo indesejado detectado";

    private static String NOT_PERMISSION_DELETE = "Você não tem permissão para deletar este usuário";

    private static String NOT_PERMISSION_UPDATE = "Você não tem permissão para alterar este usuário";

    private static final Integer MAX_IMMUTABLE_RECORDS = 3;

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
        Long immutableCount = userRepository.countByImmutableTrue();
        if (immutableCount >= MAX_IMMUTABLE_RECORDS){
            obj.setImmutable(false);
        } else {
            obj.setImmutable(true);
        }
        obj.setId(null);
        obj.setImmutable(false);
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
        if (newObj.getImmutable().equals(true)){
            throw new NotPermissionImmutableData(NOT_PERMISSION_UPDATE);
        }
        newObj.setName(obj.getName());
        newObj.setEmail(obj.getEmail());
        newObj.setImmutable(false);
        return this.userRepository.save(newObj);
    }

    public void delete(Long id){
        User obj = findById(id);
        if (obj.getImmutable().equals(true)){
            throw new NotPermissionImmutableData(NOT_PERMISSION_DELETE);
        }
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

    public User fromDTO(@Valid UserCreateDTO obj){
        User user = new User();
        user.setName(obj.getName());
        user.setEmail(obj.getEmail());
        return user;
    }

    public User fromDTO(@Valid UserUpdateDTO obj){
        User user = new User();
        user.setId(obj.getId());
        user.setName(obj.getName());
        user.setEmail(obj.getEmail());
        return user;
    }

}
