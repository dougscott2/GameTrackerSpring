package com.theironyard.services;

import com.theironyard.entities.Game;
import com.theironyard.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by DrScott on 11/13/15.
 */
public interface GameRepository extends PagingAndSortingRepository<Game, Integer> {
    Page<Game> findAllBySystem(Pageable pageable, String system);
    Page<Game> findAllByUser(Pageable pageable, User user);

    @Query("SELECT g FROM Game g WHERE LOWER(title) LIKE '%' || LOWER(?) || '%'")
    Page<Game> searchByName(Pageable pageable, String search);

}
