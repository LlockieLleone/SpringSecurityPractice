package com.lingjun.springsecuritypractice.security.service;

import com.lingjun.springsecuritypractice.security.model.SecurityUser;
import com.lingjun.springsecuritypractice.security.model.User;
import com.mongodb.client.result.UpdateResult;
import jakarta.annotation.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class SecurityUserService {

    @Resource
    private MongoTemplate mongoTemplate;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public User findUserByUsername(String username) {

        User user = mongoTemplate
                .findOne(new Query().addCriteria(Criteria.where("username").is(username)), User.class, "User");

        return user;

    }

    public String changePassword(String username, String password) {

        Query query = new Query().addCriteria(Criteria.where("username").is(username));

        Update update = new Update().set("password", bCryptPasswordEncoder.encode(password));

        UpdateResult result = mongoTemplate.upsert(query, update, SecurityUser.class, "SecurityUser");

        return result.getModifiedCount() > 0 ? "success" : "fail";

    }

    public boolean matchPassword(String username, String password) {

        User user = findUserByUsername(username);

        return bCryptPasswordEncoder.matches(password, user.getPassword());

    }

}
