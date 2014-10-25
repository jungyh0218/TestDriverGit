package sselab.yoohee.drivergen;

import sselab.cadd.cfg.expression.Expression;

/**
 * A single binary expression & and negation of it
 * @version 2014-10-25
 * @author yoohee
 *
 */
public class TerminalExpr extends Expr{

	public TerminalExpr(Expression _expression){
		super.code = _expression.getRawString();
	}
	public TerminalExpr(String neg, Expression operand2){
		super.code = neg + operand2.getRawString();
	}
	public TerminalExpr(Expression operand1, String op, Expression operand2){
		super.code = operand1.getRawString()+" == "+operand2.getRawString();
	}
	public String getCode(){
		return code;
	}
}
