package com.example.socialnetworkgui.infrastructure;

import com.example.socialnetworkgui.domain.Entity;
import com.example.socialnetworkgui.exceptions.RepoException;
import com.example.socialnetworkgui.exceptions.ValidationException;

/**
 * CRUD operations socialnetwork.repository interface
 *
 * @param <ID> - type E must have an attribute of type ID
 * @param <E>  -  type of entities saved in socialnetwork.repository
 */

public interface Repository<ID, E extends Entity<ID>> {

    /**
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    E findOne(ID id) throws RepoException;

    /**
     * @return all entities
     */
    Iterable<E> findAll();

    /**
     * @param entity entity must be not null
     * @throws ValidationException      if the entity is not valid
     * @throws RepoException      if the id already exists
     * @throws IllegalArgumentException if the given entity is null.     *
     */
    void save(E entity) throws ValidationException, RepoException;


    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @throws IllegalArgumentException if the given id is null.
     */
    void delete(ID id) throws RepoException;

    /**
     * @param entity entity must not be null
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidationException      if the entity is not valid.
     */
    void update(E entity) throws ValidationException;

}

