package sselab.yoohee.drivergen;

import java.util.ArrayList;

import sselab.cadd.cfg.expression.Expression;
/**
 * @version 2014-10-25
 * @author yoohee
 *
 */
public class ConcatExpr extends Expr{
	Expr lchild = null;
	String operator = "";
	Expr rchild = null;
	
	boolean negation = false;
	/*public ConcatExpr(TerminalExpr terminal){
		this.lchild = terminal;
	}
	public ConcatExpr(TerminalExpr l, String op, ConcatExpr r){
		this.lchild = l;
		this.operator = op;
		this.rchild = r;
	}
	public ConcatExpr(ConcatExpr l, String op, ConcatExpr r){
		this.lchild = l;
		this.operator = op;
		this.rchild = r;
	}
	public ConcatExpr(ConcatExpr l, String op, TerminalExpr r){
		this.lchild = l;
		this.operator = op;
		this.rchild = r;
	}*/
	public ConcatExpr(Expr terminal){
		this.lchild = terminal;
	}
	public ConcatExpr(String neg, Expr expr){
		if(neg.equals("!"))
			this.negation = true;
		else
			this.negation = false;
		this.lchild = expr;
	}
	public ConcatExpr(Expr l, String op, Expr r){
		this.lchild = l;
		this.operator = op;
		this.rchild = r;
	}
	public String getCode(){
		String code = "";
		if(negation == true)
			code += "!(";
		code += lchild.getCode();
		if(rchild != null){
		code += " " + operator + " ";
		code += rchild.getCode();
		}
		if(negation == true)
			code += ")";
		return code;
	}

	
	
	
	
}
