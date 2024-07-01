package com.plataform.courses.services;

import java.time.LocalDateTime;
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
import com.plataform.courses.services.exceptions.UserInactiveUpdateException;

import jakarta.validation.Valid;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    private static String UNTANTED_CONTENT = "Conteúdo indesejado detectado";

    private static String NOT_PERMISSION_DELETE = "Você não tem permissão para deletar este usuário, por favor, crie um usuário novo para que possa deleta-lo";

    private static String NOT_PERMISSION_UPDATE = "Você não tem permissão para alterar este usuário, por favor, crie um usuário novo para que possa altera-lo";

    private static String INATIVE_USER = "Você não pode atualizar as informações de um usuário inativo";

    private static final Integer MAX_IMMUTABLE_RECORDS = 3;

    private ContentFilterService filterService = new ContentFilterService();

    public String generateUserNotFoundMessage(Long id){
        return "Usuário não encontrado! Id: " + id + ", Tipo: " + User.class.getName();
    }

    public User findById(Long id){
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(()-> new ObjectNotFoundException(
            generateUserNotFoundMessage(id)
        ));
    }

    public User generateSetsCreateDTO(UserCreateDTO obj){
        User user = new User();
        user.setName(obj.getName());
        user.setEmail(obj.getEmail());
        return user;
    }

    public User generateSetsUpdateDTO(UserUpdateDTO obj){
        User user = new User();
        user.setId(obj.getId());
        user.setName(obj.getName());
        user.setEmail(obj.getEmail());
        return user;
    }

    public void checkIfIsImmutable(User obj, String str){
        if (obj.getImmutable().equals(true)){
            throw new NotPermissionImmutableData(str);
        }
    }

    public User makeUserInactive(User obj){
        obj.setActive(false);
        obj.setDeleted_at(LocalDateTime.now());
        return obj;
    }

    public User countImmutableRecords(User obj){
        Long immutableCount = userRepository.countByImmutableTrue();
        if (immutableCount >= MAX_IMMUTABLE_RECORDS){
            obj.setImmutable(false);
        } else {
            obj.setImmutable(true);
        }
        return obj;
    }

    public User setIdNull(User obj){
        obj.setId(null);
        return obj;
    }

    public void checkUserInative(User user){
        if (user.getActive().equals(false)){
            throw new UserInactiveUpdateException(INATIVE_USER);
        }
    }

    @Transactional
    public User create(User obj){;
        List<String> fieldsToCheck = Arrays.asList(obj.getName(), obj.getEmail());
        checkBadWord(fieldsToCheck);
        User newUser = countImmutableRecords(obj);
        newUser = setIdNull(newUser);
        return this.userRepository.save(newUser);
    }

    public void checkBadWord(List<String> fields){
        if (filterService.containsBadWord(fields)) {
            throw new BadWordException(UNTANTED_CONTENT);
        }
    }

    public User generateSetsUpdateUser(User obj){
        obj.setName(obj.getName());
        obj.setEmail(obj.getEmail());
        obj.setImmutable(false);
        return obj;
    }

    @Transactional
    public User update(User obj){
        List<String> fieldsToCheck = Arrays.asList(obj.getName(), obj.getEmail());
        User newObj = findById(obj.getId());
        checkBadWord(fieldsToCheck);
        checkIfIsImmutable(newObj, NOT_PERMISSION_UPDATE);
        checkUserInative(newObj);
        newObj = generateSetsUpdateUser(obj);
        return this.userRepository.save(newObj);
    }

    public void soft_delete(Long id){
        User obj = findById(id);
        checkIfIsImmutable(obj, NOT_PERMISSION_DELETE);
        obj = makeUserInactive(obj);
        this.userRepository.save(obj);
    }

    public List<User> getAll() {
        List<User> courses = this.userRepository.findAll();
        return courses;
    }

    public List<User> findByActiveTrue(){
        List<User> activeUsers = this.userRepository.findByActiveTrue();
        return activeUsers; 
    }

    public List<User> findByActiveFalse(){
        List<User> inativeUsers = this.userRepository.findByActiveFalse();
        return inativeUsers; 
    }


    public User fromDTO(@Valid UserCreateDTO obj){
        User user = generateSetsCreateDTO(obj);
        return user;
    }

    public User fromDTO(@Valid UserUpdateDTO obj){
        User user = generateSetsUpdateDTO(obj);
        return user;
    }

}
