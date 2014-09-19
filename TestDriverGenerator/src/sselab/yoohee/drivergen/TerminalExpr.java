package sselab.yoohee.drivergen;

import sselab.cadd.cfg.expression.Expression;

/**
 * @version 2014-09-19
 * @author yoohee
 *
 */
public class TerminalExpr extends Expr{
	
	public TerminalExpr(Expression _expression){
		super.code = _expression.getRawString();
	}
	
	public String getCode(){
		return code;
	}
}
