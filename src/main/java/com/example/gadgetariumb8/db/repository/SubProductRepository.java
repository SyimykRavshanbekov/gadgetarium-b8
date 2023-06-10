package com.example.gadgetariumb8.db.repository;

import com.example.gadgetariumb8.db.model.SubProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubProductRepository extends JpaRepository<SubProduct, Long> {

    @Query("SELECT u.lastViews from User u where u.id = ?1")
    List<SubProduct> getAllLastReviews(Long userId);

    @Modifying
    @Query(nativeQuery = true, value = "delete from orders_sub_products osp where osp.sub_products_id = ?1")
    void deleteFromOrders(Long id);

    @Modifying
    @Query(nativeQuery = true, value = "delete from user_basket ub where ub.basket_key = ?1")
    void deleteFromBaskets(Long id);

    @Modifying
    @Query(nativeQuery = true, value = "delete from users_comparisons uc where uc.comparisons_id = ?1")
    void deleteFromComparisons(Long id);

    @Modifying
    @Query(nativeQuery = true, value = "delete from users_favorites uf where uf.favorites_id = ?1")
    void deleteFromFavorites(Long id);

    @Modifying
    @Query(nativeQuery = true, value = "delete from users_last_views ulv where ulv.last_views_id = ?1")
    void deleteFromLastViews(Long id);

    @Modifying
    @Query("delete from SubProduct s where s.id = ?1")
    void deleteSubProduct(Long id);
}