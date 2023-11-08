package com.sidepj.ithurts.service;


import com.sidepj.ithurts.service.searchConditions.SearchCondition;

import java.io.IOException;
import java.util.List;

public interface OpenAPIDataService<T> {
    public List<T> retrieve(SearchCondition searchCondition) throws IOException;

}
