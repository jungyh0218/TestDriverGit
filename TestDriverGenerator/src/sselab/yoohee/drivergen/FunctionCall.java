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
 * @version 2014-10-14
 * @author yoohee
 *
 */
public class FunctionCall {
	private String code = ""; //the source code of generated constraints.
	ArrayList<Expression> paramList = new ArrayList<Expression>();
	//The queue of gathered constraints
	private Queue<Expr> queue = new LinkedList<Expr>(); 
	private Statement statement = null;
	
	public FunctionCall(Statement statement){
		this.statement = statement;
		initialize(this.statement);
	}
	
	/**
	 * generate assert condition string and return
	 * @return code
	 */
	public String getCode(){
		gatherConstraints();
		code = concatConstraints("&&").getCode();
		return code;
	}
	private void initialize(Statement statement){
		CallExpression callExpr = (CallExpression) statement.getExpression();
		Expression[] params = callExpr.getArguments();
		Collections.addAll(paramList,  params);
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
		boolean gatheredAll = false;
		/*while(!gatheredAll){
			for(Statement b : beforeStatements){
				currentExpr = b.getExpression();
				currentExprString = currentExpr.getRawString();
				//if current statement doesn't have its string value(before function head)
				if(currentExprString.equals("")){
					beforeStatements = currentStatement.getBeforeNodes();
					continue;
				}
				if(currentExpr instanceof BinaryExpression){
					gatherBinaryExpr((BinaryExpression)currentExpr, paramList);
				}		
			}
			if(paramList.isEmpty())
				gatheredAll = true;
			beforeStatements = currentStatement.getBeforeNodes();
		}*/
		
		gatherFromEachBeforeNodes(beforeStatements, paramList);
	}
	
	private void gatherFromEachBeforeNodes(
		ArrayList<Statement> beforeStmts, ArrayList<Expression>params){
		ArrayList<Statement> before = beforeStmts;
		if(params.isEmpty()){
			return;
		}
		for(Statement b : before){
			Expression current = b.getExpression();
			@SuppressWarnings("unchecked")
			ArrayList<Expression>cloneList=(ArrayList<Expression>)params.clone();
			if(current instanceof BinaryExpression)
				gatherBinaryExpr((BinaryExpression)current, cloneList);
			gatherFromEachBeforeNodes(b.getBeforeNodes(), cloneList);
		}
		//ConcatExpr temp = concatConstraints("||");
				//queue.offer(temp);
		
	}
	
	
	/**
	 * concatenate TerminalExpr constraints 
	 * and make a new ConcatExpr object of full constraint.
	 * Have right recursion.
	 * @return concat
	 */
	private ConcatExpr concatConstraints(String operator){
		ConcatExpr concat = null;
		
		while(!queue.isEmpty()){
			ConcatExpr rchild = null;
			if(concat == null){
				concat = new ConcatExpr(queue.poll());
			}
			else{
				rchild = concat;
				concat = new ConcatExpr(
						queue.poll(), operator, rchild);
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
		
		
		Expression lChild = currentExpr.getOperand1();
		Expression rChild = currentExpr.getOperand2();
		//if the operand1 is id name, then it's a terminal node.
		if(lChild instanceof IdExpression || lChild instanceof UnaryExpression){
			if(currentExpr.getOperator() == Operator.ASSIGN){
				queue.offer(new TerminalExpr(currentExpr.getOperand1(),
						"==", currentExpr.getOperand2()));
			}
			else
				queue.offer(new TerminalExpr(currentExpr));
			
			removeFromList(currentExpr, paramList);
			return;
		}
		
		gatherBinaryExpr((BinaryExpression)lChild, paramList);
		gatherBinaryExpr((BinaryExpression)rChild, paramList);
		
		
	}
}
