package sselab.yoohee.drivergen;

import sselab.cadd.cfg.expression.Expression;

/**
 * A single binary expression
 * @version 2014-10-02
 * @author yoohee
 *
 */
public class TerminalExpr extends Expr{
	
	public TerminalExpr(Expression _expression){
		super.code = _expression.getRawString();
	}
	public TerminalExpr(Expression operand1, String op, Expression operand2){
		super.code = operand1.getRawString()+" == "+operand2.getRawString();
	}
	public String getCode(){
		return code;
	}
}
