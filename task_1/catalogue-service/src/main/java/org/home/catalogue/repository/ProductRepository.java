package org.home.catalogue.repository;

import org.home.catalogue.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    @Query(name = "Product.findAllByTitleLikeIgnoringCase")
    Iterable<Product> findAllByTitleLikeIgnoreCase(@Param("filter")String filter);

}
