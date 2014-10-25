package sselab.yoohee.drivergen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import sselab.cadd.cfg.expression.Expression;
import sselab.cadd.cfg.expression.type.*;
import sselab.cadd.cfg.node.BranchStatement;
import sselab.cadd.cfg.node.RefStatement;
import sselab.cadd.cfg.node.Statement;
import sselab.cadd.cfg.node.specifiednode.DeclarationStatement;
import sselab.cadd.variable.Operator;

/**
 * FunctionCall class
 * This class contains Statement, CallExpression 
 * and the code of generated(concatenated) statement
 * @version 2014-10-25
 * @author yoohee
 *
 */
public class FunctionCall {
	private String code = ""; //the source code of generated constraints.
	ArrayList<Expression> paramList = new ArrayList<Expression>();
	//The queue of gathered constraints
	private Stack<Expr> exprStack = new Stack<Expr>(); 
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
		gatherConstraintsFromEachBeforeNodes(beforeStatements, paramList);
	}
	
	private void gatherConstraintsFromEachBeforeNodes(
		ArrayList<Statement> beforeStmts, ArrayList<Expression>params){
		ArrayList<Statement> before = beforeStmts;
		if(params.isEmpty()){
			return;
		}
		for(Statement b : before){
			Expression current = b.getExpression();
			@SuppressWarnings("unchecked")
			ArrayList<Expression>cloneList=(ArrayList<Expression>)params.clone();
			if(b instanceof BranchStatement){
				//now: only can process atomic binary condition.
				//have to fixed later-->to process complex binary conditions.
				gatherBinaryExpr((BinaryExpression)current, cloneList);
				Expr branch = exprStack.pop();
				Expr child = exprStack.peek();
				//if 'else' case, add negation.
				if(!getThenOrElse((BranchStatement)b, child)){
					ConcatExpr temp = new ConcatExpr("!", branch);
					exprStack.push(temp);
				}
			}
			else if(current instanceof BinaryExpression){
				gatherBinaryExpr((BinaryExpression)current, cloneList);
			}
			if(!cloneList.isEmpty()){
				gatherConstraintsFromEachBeforeNodes(b.getBeforeNodes(), cloneList);
			}
			pushGatheredConstraintsIntoStack();
			ConcatExpr eachPathConstraint = concatConstraints("||");
			exprStack.push(eachPathConstraint);
		}
	}
	void pushGatheredConstraintsIntoStack(){
		Expr temp1 = null;		
		Expr temp2 = null;
		if(!exprStack.empty()){
			temp1 = exprStack.pop();
			if(!exprStack.empty())
				temp2 = exprStack.pop();
		}
		if(temp2 == null){
			exprStack.push(temp1);
		}
		else
			exprStack.push(new ConcatExpr(temp1, "&&", temp2));
	}

	
	boolean getThenOrElse(BranchStatement branch, Expr child){
		Statement branchToCompare = branch.getFalseNode();
		Expression exprToCompare_original = branchToCompare.getExpression();
		if(exprToCompare_original instanceof BinaryExpression){
			BinaryExpression exprToCompare =
					(BinaryExpression)exprToCompare_original;
			Expr temp = null;
			if(exprToCompare.getOperand1() instanceof IdExpression 
					|| exprToCompare.getOperand() instanceof UnaryExpression){
				if(exprToCompare.getOperator() == Operator.ASSIGN){
					temp = new TerminalExpr(
							((BinaryExpression) exprToCompare).getOperand1(),
							"==",
							((BinaryExpression) exprToCompare).getOperand2()
						);			
				}
				else{
				temp = new TerminalExpr(exprToCompare);
				}
				
				if(temp.getCode().equals(child.getCode()))
					return false; //else statement
			}	
		}
		return true;
	}
	/**
	 * concatenate TerminalExpr constraints 
	 * and make a new ConcatExpr object of full constraint.
	 * Have right recursion.
	 * @return concat
	 */
	private ConcatExpr concatConstraints(String operator){
		ConcatExpr concat = null;
		
		while(!exprStack.isEmpty()){
			ConcatExpr lchild = null;
			if(concat == null){
				concat = new ConcatExpr(exprStack.pop());
			}
			else{
				lchild = concat;
				concat = new ConcatExpr(
						lchild, operator, exprStack.pop());
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
				this.exprStack.push(new TerminalExpr(currentExpr.getOperand1(),
						"==", currentExpr.getOperand2()));
			}
			else
				exprStack.push(new TerminalExpr(currentExpr));
			
			removeFromList(currentExpr, paramList);
			return;
		}
		
		gatherBinaryExpr((BinaryExpression)lChild, paramList);
		gatherBinaryExpr((BinaryExpression)rChild, paramList);
		
		
	}
}
