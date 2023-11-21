package com.example.taskorganization.model.criteria;

import com.example.taskorganization.model.enums.SortDirection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortingCriteria {

    private SortDirection createdAt;


    private SortDirection name;


    private SortDirection priority;


    private SortDirection deadline;

}
