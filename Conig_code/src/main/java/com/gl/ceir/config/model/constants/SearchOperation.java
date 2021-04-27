package com.gl.ceir.config.model.constants;

public enum SearchOperation {
	EQUALITY, EQUALITY_CASE_INSENSITIVE, NEGATION, GREATER_THAN, LESS_THAN, LIKE;

	public static final String[] SIMPLE_OPERATION_SET = { ":", "!", ">", "<", "~" };

	public static SearchOperation getSimpleOperation(final char input)
	{
		switch (input) {
		case ':': return EQUALITY;
		case '!': return NEGATION;
		case '>': return GREATER_THAN;
		case '<': return LESS_THAN;
		case '~': return LIKE;
		default: return null;
		}
	}
}
