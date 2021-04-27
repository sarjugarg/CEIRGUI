package com.gl.ceir.config.specificationsbuilder;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import com.gl.ceir.config.model.SearchCriteria;
import com.gl.ceir.config.model.User;
import com.gl.ceir.config.model.Usertype;

public class Joiner<T> {

	public Specification<T> joinToUsersAndUsertype(SearchCriteria searchCriteria){
		return (root, query, cb) -> {
			Join<T, User> user = root.join("user".intern());
			Join<User, Usertype> usertype = user.join("usertype".intern());
			return cb.like(usertype.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
		}; 
	}
	
}
