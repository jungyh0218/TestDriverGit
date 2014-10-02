package sselab.yoohee.drivergen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import sselab.cadd.cfg.expression.Expression;
import sselab.cadd.cfg.expression.type.*;
import sselab.cadd.cfg.node.Statement;
import sselab.cadd.cfg.node.specifiednode.DeclarationStatement;
import sselab.cadd.variable.Operator;

/**
 * FunctionCall class
 * This class contains Statement, CallExpression 
 * and the code of generated(concatenated) statement
 * @version 2014-10-02
 * @author yoohee
 *
 */
public class FunctionCall {
	private String code = ""; //the source code of generated constraints.
	public CallExpression callExpr = null;
	//the stack to make a concatenated expression
	private Queue<TerminalExpr> queue = new LinkedList<TerminalExpr>(); 
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
		Statement currentStatement = statement;
		ArrayList<Statement> beforeStatements = 
				currentStatement.getBeforeNodes();
		Expression currentExpr = currentStatement.getExpression();
		String currentExprString;
		Expression[] params = ((CallExpression)currentExpr).getArguments();
		ArrayList<Expression>paramList = new ArrayList<Expression>();
		Collections.addAll(paramList, params);
		boolean gatheredAll = false;
		while(!gatheredAll){
			currentStatement = beforeStatements.get(0);
			currentExpr = beforeStatements.get(0).getExpression();
			currentExprString = currentExpr.getRawString();
			//if current statement has its expression string(not RefState or NullExpression)
			if(!currentExprString.equals("")){
				if(currentExpr instanceof BinaryExpression){
					gatherBinaryExpr((BinaryExpression)currentExpr, paramList);
				}		
			}
			if(paramList.isEmpty())
				gatheredAll = true;
			beforeStatements = currentStatement.getBeforeNodes();
		}
	}
	
	/**
	 * concatenate TerminalExpr constraints 
	 * and make a new ConcatExpr object of full constraint.
	 * Have right recursion.
	 * @return concat
	 */
	private ConcatExpr concatConstraints(){
		ConcatExpr concat = null;
		
		while(!queue.isEmpty()){
			ConcatExpr rchild = null;
			if(concat == null){
				concat = new ConcatExpr(queue.poll());
			}
			else{
				rchild = concat;
				concat = new ConcatExpr(queue.poll(), "&&", rchild);
			}
		}
		return concat;
	}
	private boolean isin(Expression id, ArrayList<Expression>params){
		boolean isIn = false;
		for(Expression p: params){
			if(id.getRawString().equals(p.getRawString()))
				isIn = true;
		}
		return isIn;
	}
	private void removeFromList(
			Expression current, ArrayList<Expression>paramList){
		Expression id = ((BinaryExpression)current).getOperand1();
		@SuppressWarnings("unchecked")
		ArrayList<Expression>cloneList=(ArrayList<Expression>)paramList.clone();
		for(Expression p : cloneList){
			if(p.getRawString().equals(id.getRawString()))
				paramList.remove(p);
		}
	}
	private void gatherBinaryExpr(
			BinaryExpression currentExpr, ArrayList<Expression>paramList){
		if(isin(currentExpr.getOperand1(),paramList)){
			if(currentExpr.getOperator() == Operator.ASSIGN){
				queue.offer(new TerminalExpr(currentExpr.getOperand1(),
						"==", currentExpr.getOperand2()));
			}
			else
				queue.offer(new TerminalExpr(currentExpr));
			
			removeFromList(currentExpr, paramList);
		}
	}
}
