package com.example.gadgetariumb8.db.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class CustomOrderRepository {
      public String getAllOrder(){
        String sql = """
                select o.id as id,concat(c.first_name,' ',c.last_name) as fio,
                      o.order_number as orderNumber,
                      o.date as createdAt, o.quantity as quantity, o.total_price as totalPrice,o.delivery_type as deliveryType,
                      o.status as status from orders o join customers c on o.customer_id = c.id where o.status = ?
                      %s %s
                """;
        return sql;
    }

    public String dateClauseFromBefore(){
          String sql = """
                  AND o.date BETWEEN '%s' AND '%s'
                  """;
          return sql;
    }

    public String dateClauseFrom(){
          String sql = """
                  AND o.date >= '%s'
                  """;
          return sql;
    }

    public String dateClauseBefore(){
        String sql = """
                  AND o.date <= '%s'
                  """;
        return sql;
    }

    public String keyWordCondition(){
          String sql = """
                  and (c.first_name ilike ? or c.last_name ilike ? or o.order_number ilike ?
                    or cast(o.total_price as text) ilike ?
                    or o.status ilike ?)
                  """;
          return sql;
    }

    public String limitOffset(){
          String sql = """
                  LIMIT ? OFFSET ?
                  """;
          return sql;
    }

    public String countSql(){
          String sql = """
                  select 
                  """;
          return sql;
    }


}
