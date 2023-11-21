package com.example.taskorganization.util;

import com.example.taskorganization.model.criteria.SortingCriteria;
import com.example.taskorganization.model.enums.SortDirection;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SortingUtil {

    private Optional<Sort.Order> getOrder(String field, SortDirection direction) {

        return Optional.ofNullable(field)
                .flatMap(f -> Optional.ofNullable(direction)
                        .map(d -> new Sort.Order(Direction.valueOf(direction.name()), f)));

    }

    public List<Sort.Order> buildSortOrders(SortingCriteria sortingCriteria) {

        List<Sort.Order> orders = new ArrayList<>();

        getOrder("createdAt", sortingCriteria.getCreatedAt()).ifPresent(orders::add);
        getOrder("name", sortingCriteria.getName()).ifPresent(orders::add);
        getOrder("priority", sortingCriteria.getPriority()).ifPresent(orders::add);
        getOrder("deadline", sortingCriteria.getDeadline()).ifPresent(orders::add);


        return orders;
    }

}
