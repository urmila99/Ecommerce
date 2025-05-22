package com.urmila.ecommerce.Repository;

import com.urmila.ecommerce.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
     Category findByCategoryName(String categoryName);
}
