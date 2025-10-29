package org.example
package interpreter

import types.Expr.*
import types.LiteralValue.LiteralInt
import types.{Expr, LiteralValue, Token, TokenType}
import strategy.MathStrategyFactory

object Interpreter {
  def interpret(expr: Expr): Option[Literal] = {
    evaluate(expr, expr)
  }

  private def evaluate(expr: Expr, firstExpression: Expr): Option[Literal] = {
    expr match {
      case Binary(left, op, right) => {
        val leftResult = evaluate(left, firstExpression).get
        val rightResult = evaluate(right, firstExpression).get
        Some(apply(leftResult, op, rightResult))
      }
      case Unary(_, _) => None
      case Literal(value) => Some(Literal(value))
      case Grouping(expr) => evaluate(expr, firstExpression)
    }
  }

  def apply(left: Literal, op: Token, right: Literal): Literal = {
    (left.value, right.value) match {
      case (LiteralInt(l), LiteralInt(r)) =>
        val strategy = MathStrategyFactory.getStrategy(op.dataType)
        strategy.execute(LiteralInt(l), LiteralInt(r))
      case _ => Literal(LiteralInt(0))
    }
  }
}
