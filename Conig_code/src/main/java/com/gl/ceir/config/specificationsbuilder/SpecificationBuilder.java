package com.gl.ceir.config.specificationsbuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.Expression;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;

import com.gl.ceir.config.model.SearchCriteria;
import com.gl.ceir.config.model.constants.Datatype;
import com.gl.ceir.config.model.constants.SearchOperation;
import com.gl.ceir.config.util.DbFunctions;

public class SpecificationBuilder<T> {

	private static final Logger logger = LogManager.getLogger(SpecificationBuilder.class);

	private final List<SearchCriteria> params;
	private final List<SearchCriteria> searchParams;
	private final String dialect;
	private List<Specification<T>> specifications;

	public SpecificationBuilder(String dialect) {
		params = new ArrayList<>();
		searchParams = new ArrayList<>();
		specifications = new LinkedList<>();
		this.dialect = dialect;
	}

	public final SpecificationBuilder<T> with(SearchCriteria criteria) { 
		params.add(criteria);
		return this;
	}

	public final SpecificationBuilder<T> orSearch(SearchCriteria criteria) { 
		searchParams.add(criteria);
		return this;
	}

	public Specification<T> build() { 
		// convert each of SearchCriteria params to Specification and construct combined specification based on custom rules.

		Specification<T> finalSpecification = null;
		Specification<T> searchSpecification  = null;
		List<Specification<T>> specifications = createSpecifications( params );

		logger.info("length on params : " + specifications.size());
		if(!specifications.isEmpty()) {
			finalSpecification = Specification.where(specifications.get(0));

			for(int i = 1; i<specifications.size(); i++) {
				finalSpecification = finalSpecification.and(specifications.get(i));
			}
		}
		
		logger.info("length on AND : " + specifications.size());
		
		if( !searchParams.isEmpty() ) {
			specifications = createSpecifications( searchParams );
			if( !specifications.isEmpty()) {
				
				searchSpecification = specifications.get(0);
				for(int i = 1; i<specifications.size() ;i++) {
					searchSpecification = searchSpecification.or(specifications.get(i));
				}
				
				finalSpecification = finalSpecification.and( searchSpecification );
			}
		}

		logger.info("length on OR : " + specifications.size());
		
		return finalSpecification;
	}

	public void addSpecification(Specification<T> specification) { 
		specifications.add(specification);
	}

	private List<Specification<T>> createSpecifications(List<SearchCriteria> criterias){

		try {
			for(SearchCriteria searchCriteria : criterias) {
				specifications.add((root, query, cb)-> {
					if(SearchOperation.GREATER_THAN.equals(searchCriteria.getSearchOperation())
							&& Datatype.STRING.equals(searchCriteria.getDatatype())) {
						return cb.greaterThan(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
					}
					else if(SearchOperation.LESS_THAN.equals(searchCriteria.getSearchOperation())
							&& Datatype.STRING.equals(searchCriteria.getDatatype())) {
						return cb.lessThan(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
					}
					else if(SearchOperation.EQUALITY.equals(searchCriteria.getSearchOperation())
							&& Datatype.STRING.equals(searchCriteria.getDatatype())) {
						return cb.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
					}
					else if(SearchOperation.GREATER_THAN.equals(searchCriteria.getSearchOperation())
							&& Datatype.DATE.equals(searchCriteria.getDatatype())){
						Expression<String> dateStringExpr = cb.function(DbFunctions.getDate(dialect), String.class, root.get(searchCriteria.getKey()), cb.literal(DbFunctions.getDateFormat(dialect)));
						return cb.greaterThan(cb.lower(dateStringExpr), searchCriteria.getValue().toString());
					}
					else if(SearchOperation.LESS_THAN.equals(searchCriteria.getSearchOperation())
							&& Datatype.DATE.equals(searchCriteria.getDatatype())){
						Expression<String> dateStringExpr = cb.function(DbFunctions.getDate(dialect), String.class, root.get(searchCriteria.getKey()), cb.literal(DbFunctions.getDateFormat(dialect)));
						return cb.lessThan(cb.lower(dateStringExpr), searchCriteria.getValue().toString());
					}
					
					else if(SearchOperation.EQUALITY.equals(searchCriteria.getSearchOperation())
							&& Datatype.DATE.equals(searchCriteria.getDatatype())){
						Expression<String> dateStringExpr = cb.function(DbFunctions.getDate(dialect), String.class, root.get(searchCriteria.getKey()), cb.literal(DbFunctions.getDateFormat(dialect)));
						return cb.equal(cb.lower(dateStringExpr), searchCriteria.getValue().toString());
					}
					
					else if(SearchOperation.LIKE.equals(searchCriteria.getSearchOperation())
							&& Datatype.STRING.equals(searchCriteria.getDatatype())) {
						return cb.like(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
					}
					
					else {
						return null;
					}
				});
			}
		}catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
		}

		return specifications;
	}

	public Specification<T> in(String key, List<Integer> status){
		return (root, query, cb) -> {
			logger.info("In query save ");
			return cb.in(root.get(key)).value(status);
		};
	}
	
	public Specification<T> notIn(String key, List<String> status){
		return (root, query, cb) -> {
			logger.info("In query save ");
			return cb.in(root.get(key)).value(status)
					.not();
		};
	}

}
