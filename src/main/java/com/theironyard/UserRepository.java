package com.theironyard;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by DrScott on 11/13/15.
 */
public interface UserRepository extends CrudRepository<User, Integer>{
    User findOneByName(String name);

}
