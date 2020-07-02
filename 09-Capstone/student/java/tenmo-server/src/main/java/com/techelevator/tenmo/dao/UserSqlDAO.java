package com.techelevator.tenmo.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.techelevator.tenmo.model.User;

@Service
public class UserSqlDAO implements UserDAO {

    private static final double STARTING_BALANCE = 1000;
    private JdbcTemplate jdbcTemplate;

    public UserSqlDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int findIdByUsername(String username) {
        return jdbcTemplate.queryForObject("select user_id from users where username = ?", int.class, username);
    }

    @Override 
    //this will only select information that is safe to send out to the client when it requests a user list. 
    //Otherwise the password hash and user roles will be transmitted over the "web" (big stinky)
    public List<User> findAllUsersSanitized() { 
    	List<User> users = new ArrayList<>();
    	String sql = "SELECT user_id, username FROM users";
    	
    	SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
    	while (results.next()) {
    		User user = mapRowToUser(results);
    		users.add(user);
    	}
    	
    	return users;
    }
    
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "select * from users";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }

        return users;
    }

    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        for (User user : this.findAll()) {
            if( user.getUsername().toLowerCase().equals(username.toLowerCase())) {
                return user;
            }
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
    }

    @Override
    public boolean create(String username, String password) {
        boolean userCreated = false;
        boolean accountCreated = false;

        // create user
        String insertUser = "insert into users (username,password_hash) values(?,?)";
        String password_hash = new BCryptPasswordEncoder().encode(password);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String id_column = "user_id";
        userCreated = jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(insertUser, new String[]{id_column});
                    ps.setString(1, username);
                    ps.setString(2,password_hash);
                    return ps;
                }
                , keyHolder) == 1;
        int newUserId = (int) keyHolder.getKeys().get(id_column);

        // create account
        String insertAccount = "insert into accounts (user_id,balance) values(?,?)";
        accountCreated = jdbcTemplate.update(insertAccount,newUserId,STARTING_BALANCE) == 1;

        return userCreated && accountCreated;
    }

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        SqlRowSetMetaData metaData = rs.getMetaData();
                
        user.setId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        //checking columns returned by rowset. if only 2 columns are returned then that means the sanitized method was called
    	//and password_hash and ROLE_USER will throw exceptions
        if (metaData.getColumnCount() > 2) {
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("ROLE_USER");
        }
        return user;
    }
}
