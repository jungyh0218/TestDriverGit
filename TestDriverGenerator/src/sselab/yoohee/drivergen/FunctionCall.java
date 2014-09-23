package sselab.yoohee.drivergen;

import java.util.ArrayList;
import java.util.Stack;

import sselab.cadd.cfg.expression.Expression;
import sselab.cadd.cfg.expression.type.*;
import sselab.cadd.cfg.node.RefStatement;
import sselab.cadd.cfg.node.Statement;
import sselab.cadd.cfg.node.specifiednode.DeclarationStatement;

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
	private Stack<TerminalExpr> stack = new Stack<TerminalExpr>(); //the stack to make a concatenated expression
	private Statement statement = null;
	public FunctionCall(Statement statement){
		this.statement = statement;
		//callExpr = (CallExpression) this.statement.getExpression();
	}
	
	/**
	 * generate assert condition string and return
	 * @return code
	 */
	public String getCode(){
		gatherConstraints();
		code = concatConstraints().getCode();
		return code;
	}
	
	/**
	 * gather constraints from CFG
	 * Rough version. Have to implement correct method later.
	 */
	public void gatherConstraints(){
		ArrayList<Statement> beforeStatements = new ArrayList<Statement>();
		Statement temp = statement;
		while(!(temp instanceof DeclarationStatement)){
			if(!temp.getBeforeNodes().get(0).getExpression().getRawString().equals("")){
				System.out.println(temp.getBeforeNodes().get(0).getExpression().getRawString());
				stack.push(new TerminalExpr(temp.getBeforeNodes().get(0).getExpression()));
			}
			temp = temp.getBeforeNodes().get(0);
		}
	}
	
	/**
	 * concatenate constraints and make a new ConcatExpr object of full constraint.
	 * @return concat
	 */
	public ConcatExpr concatConstraints(){
		ConcatExpr concat = null;
		
		while(!stack.isEmpty()){
			ConcatExpr rchild = null;
			if(concat == null){
				concat = new ConcatExpr(stack.pop());
			}
			else{
				rchild = concat;
				concat = new ConcatExpr(stack.pop(), "&&", rchild);
			}
		}
		return concat;
	}
	
	
}
