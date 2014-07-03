package org.svnrepoviewer.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.svnrepoviewer.domain.Repository;

/**
 * @author smalafeev
 */
public interface RepositoryDao extends CrudRepository<Repository, Long> {

    @Query("select r from Repository r where r.url = :url")
    Repository get(@Param("url") String url);

    @Query("select r from Repository r order by r.url")
    List<Repository> getAll();
}
