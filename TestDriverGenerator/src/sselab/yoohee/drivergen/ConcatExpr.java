package sselab.yoohee.drivergen;

import java.util.ArrayList;

import sselab.cadd.cfg.expression.Expression;
/**
 * @version 2014-09-19
 * @author yoohee
 *
 */
public class ConcatExpr extends Expr{
	TerminalExpr lchild = null;
	String operator = "";
	ConcatExpr rchild = null;
	
	public ConcatExpr(TerminalExpr terminal){
		this.lchild = terminal;
	}
	public ConcatExpr(TerminalExpr l, String op, ConcatExpr r){
		this.lchild = l;
		this.operator = op;
		this.rchild = r;
	}

	
	
	
	
}
