package sselab.yoohee.drivergen;

import java.util.Stack;

import sselab.cadd.cfg.expression.type.*;
import sselab.cadd.cfg.node.Statement;

/**
 * FunctionCall class
 * This class contains Statement, CallExpression 
 * and the code of generated(concatenated) statement
 * @version 2014-09-16
 * @author yoohee
 *
 */
public class FunctionCall {
	private String code = ""; //the source code of generated constraints.
	public CallExpression callExpr = null;
	private Stack<Expr> stack; //the stack to make a concatenated expression
	private Statement statement = null;
	public FunctionCall(Statement statement){
		this.statement = statement;
		callExpr = (CallExpression) this.statement.getExpression();
	}
	
	public String getCode(){
		return code;
	}
	
	
}
