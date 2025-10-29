package org.example
package types

enum Expr {
  case Binary(left: Expr, op: Token, right: Expr)
  case Unary(op: Token, right: Expr)
  case Literal(value: LiteralValue)
  case Grouping(expr: Expr)
}