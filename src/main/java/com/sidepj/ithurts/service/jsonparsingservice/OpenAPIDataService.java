package com.sidepj.ithurts.service.jsonparsingservice;


import com.sidepj.ithurts.service.searchConditions.SearchCondition;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
public interface OpenAPIDataService<T> {
    public List<T> retrieve(SearchCondition searchCondition);

//    public T retrieveOne(SearchCondition searchCondition);
}
