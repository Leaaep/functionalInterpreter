package org.example
package parser

import types.Expr.*
import types.LiteralValue.{LiteralBool, LiteralNil, LiteralString}
import types.TokenType.*
import types.{Expr, LiteralValue, Token, TokenType}

import scala.annotation.tailrec

object Parser {
  def parse(tokens: List[Token]): Option[Expr] =
    expression(tokens) match {
      case Some(tokens, expr) => Some(expr)
      case None => None
    }

  private def expression(tokens: List[Token]): Option[(List[Token], Expr)] =
    equality(tokens).orElse(None)

  private def equality(tokens: List[Token]): Option[(List[Token], Expr)] =
    comparison(tokens) match {
      case Some(tokens, expr) => {
        @tailrec
        def loop(tokens: List[Token], expr: Expr): Option[(List[Token], Expr)] =
          matchType(List(EQUAL_EQUAL, BANG_EQUAL), tokens) match {
            case Some(rest) =>
              val operator = tokens.head
              comparison(rest) match {
                case Some(tokens, right) => loop(tokens, Binary(expr, operator, right))
                case None => None
              }
            case None => Some(tokens, expr)
          }
        loop(tokens, expr)
      }
      case None => None
    }

  private def comparison(tokens: List[Token]): Option[(List[Token], Expr)] =
    term(tokens) match {
      case Some(tokens, expr) => {
        @tailrec
        def loop(tokens: List[Token], expr: Expr): Option[(List[Token], Expr)] =
          matchType(List(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL), tokens) match {
            case Some(rest) =>
              val operator = tokens.head
              term(rest) match {
                case Some(tokens, right) => loop(tokens, Binary(expr, operator, right))
                case None => None
              }
            case None => Some(tokens, expr)
          }
        loop(tokens, expr)
      }
      case None => None
    }

  private def term(tokens: List[Token]): Option[(List[Token], Expr)] =
    factor(tokens) match {
      case Some(tokens, expr) => {
        @tailrec
        def loop(tokens: List[Token], expr: Expr): Option[(List[Token], Expr)] =
          matchType(List(MINUS, PLUS), tokens) match {
            case Some(rest) =>
              val operator = tokens.head
              factor(rest) match {
                case Some(tokens, right) => loop(tokens, Binary(expr, operator, right))
                case None => None
              }
            case None => Some(tokens, expr)
          }

        loop(tokens, expr)
      }
      case None => None
    }


  private def factor(tokens: List[Token]): Option[(List[Token], Expr)] =
    unary(tokens) match {
      case Some(tokens, expr) => {
        @tailrec
        def loop(tokens: List[Token], expr: Expr): Option[(List[Token], Expr)] =
          matchType(List(SLASH, STAR), tokens) match {
            case Some(rest) =>
              val operator = tokens.head
              unary(rest) match {
                case Some(tokens, right) => loop(tokens, Binary(expr, operator, right))
                case None => None
              }
            case None => Some(tokens, expr)
          }
        loop(tokens, expr)
      }
      case None => None
    }

  private def unary(tokens: List[Token]): Option[(List[Token], Expr)] =
    matchType(List(BANG, MINUS), tokens) match {
      case Some(rest) =>
        val operator = tokens.head
        unary(rest) match {
          case Some(list, right) => Some(list, Unary(operator, right))
          case None => None
        }
      case None => primary(tokens).orElse(None)
    }


  private def primary(tokens: List[Token]): Option[(List[Token], Expr)] =
    matchType(List(TRUE), tokens)
      .map(token => (token, Literal(LiteralBool(true))))
      .orElse(
        matchType(List(FALSE), tokens).map(token => (token, Literal(LiteralBool(false))))
      )
      .orElse(
        matchType(List(NIL), tokens).map(token => (token, Literal(LiteralNil)))
      )
      .orElse(
        matchType(List(NUMBER, STRING), tokens).map { list =>
          val head = tokens.head
          val expr = head.dataType match {
            case TokenType.NUMBER => Literal(LiteralValue.LiteralInt(head.data.toInt))
            case TokenType.STRING => Literal(LiteralString(head.data))
            case _ => Literal(LiteralNil)
          }
          (list, expr)
        }
      )
      .orElse(
        matchType(List(LEFT_PAREN), tokens).flatMap { afterLeft =>
          expression(afterLeft) match {
            case Some(tokens, expr) => {
              matchType(List(RIGHT_PAREN), tokens)
                .map(afterRight => (afterRight, Grouping(expr)))
            }
            case None => None
          }
        }
      )


  private def matchType(tokenTypes: List[TokenType], tokens: List[Token]): Option[List[Token]] = {
    if (tokenTypes.exists(tokenType => checkType(tokenType, tokens))) {
      val result = eat(tokens)
      Some(result._2)
    } else None
  }

  private def eat(tokens: List[Token]): (Token, List[Token]) = {
    val lastToken = tokens.head
    (lastToken, tokens.tail)
  }

  private def checkType(tokenType: TokenType, tokens: List[Token]): Boolean = {
    tokens.head.dataType == tokenType
  }
}
