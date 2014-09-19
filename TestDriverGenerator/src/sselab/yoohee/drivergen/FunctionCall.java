package sselab.yoohee.drivergen;

import java.util.ArrayList;
import java.util.Stack;

import sselab.cadd.cfg.expression.Expression;
import sselab.cadd.cfg.expression.type.*;
import sselab.cadd.cfg.node.RefStatement;
import sselab.cadd.cfg.node.Statement;

/**
 * FunctionCall class
 * This class contains Statement, CallExpression 
 * and the code of generated(concatenated) statement
 * @version 2014-09-19
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
	
	/**
	 * generate assert condition string and return
	 * @return code
	 */
	public String getCode(){
		code = concatConstraints().getCode();
		return code;
	}
	
	/**
	 * gather constraints from CFG
	 */
	public void gatherConstraints(){
		ArrayList<Statement> beforeStatements = statement.getBeforeNodes();
		for(Statement s : beforeStatements){
			if(s instanceof RefStatement){
				//TODO: set statement 'marked' <-- but how? ask it to KDW.
				System.out.println(s.getExpression().getRawString());
				Expr e = new TerminalExpr(s.getExpression());
				stack.push(e);
			}
		}
	}
	
	/**
	 * concatenate constraints and make a new ConcatExpr object of full constraint.
	 * @return concat
	 */
	public ConcatExpr concatConstraints(){
		ConcatExpr concat = null;
		ConcatExpr rchild = null;
		
		Expr e = stack.pop();
		if(stack.isEmpty()){
			rchild = new ConcatExpr((TerminalExpr)e);
			if(concat == null){
				concat = rchild;
				return concat;
			}
		}
		concat = new ConcatExpr((TerminalExpr)e, "&&", concatConstraints());		
		return concat;
	}
	
	
}
