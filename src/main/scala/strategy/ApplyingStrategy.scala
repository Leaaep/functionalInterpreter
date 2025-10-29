package org.example
package strategy

import types.Expr.Literal
import types.LiteralValue.LiteralInt
import types.{LiteralValue, TokenType}

trait MathStrategy {
  def execute(left: LiteralInt, right: LiteralInt): Literal
}

object MathStrategyFactory {
  def getStrategy(operation: TokenType): MathStrategy = {
    operation match {
      case TokenType.PLUS => new MathStrategy {
        private val add: Int => Int => Int = x => y => x + y
        override def execute(left: LiteralInt, right: LiteralInt): Literal =
          Literal(LiteralInt(add(left.int)(right.int)))
      }
      case TokenType.MINUS => new MathStrategy {
        private val subtract = (x: Int) => (y: Int) => x - y
        override def execute(left: LiteralInt, right: LiteralInt): Literal =
          Literal(LiteralInt(subtract(left.int)(right.int)))
      }
      case TokenType.STAR => new MathStrategy {
        private val multiply = (x: Int) => (y: Int) => x * y
        override def execute(left: LiteralInt, right: LiteralInt): Literal =
          Literal(LiteralInt(multiply(left.int)(right.int)))
      }
      case TokenType.SLASH => new MathStrategy {
        // closure for safe division
        private val safeDivide = {
          val handleZero = (x: Int, y: Int) => if y == 0 then 0 else x / y
          (x: Int) => (y: Int) => handleZero(x, y)
        }
        override def execute(left: LiteralInt, right: LiteralInt): Literal =
          Literal(LiteralInt(safeDivide(left.int)(right.int)))
      }
      case _ => new MathStrategy {
        override def execute(left: LiteralInt, right: LiteralInt): Literal =
          Literal(LiteralInt(0))
      }
    }
  }
}
