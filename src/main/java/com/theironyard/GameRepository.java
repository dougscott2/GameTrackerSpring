package com.theironyard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by DrScott on 11/13/15.
 */
public interface GameRepository extends CrudRepository<Game, Integer>{
    List<Game> findAllBySystem(String system);

    @Query("SELECT g FROM Game g WHERE LOWER(title) LIKE '%' || LOWER(?) || '%'")
    List<Game> searchByName(String search);
}
