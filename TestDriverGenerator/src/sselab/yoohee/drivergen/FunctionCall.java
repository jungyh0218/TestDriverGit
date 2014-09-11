package sselab.yoohee.drivergen;

import java.beans.Expression;
import java.util.Stack;

public class FunctionCall {
	private String code = ""; //the source code of generated constraints.
	Stack<Expression> stack; //the stack to make a concatenated expression
	
	public String getCode(){
		return code;
	}
	
	
}
