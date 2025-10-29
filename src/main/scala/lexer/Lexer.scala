package org.example
package lexer

import types.TokenType.*
import types.{Token, TokenType}

import scala.annotation.tailrec

object Lexer {
  def lex(input: String): List[Token] =
    val inputChars = input.toList

    @tailrec
    def loop(chars: List[Char], currentToken: Token, tokens: List[Token], index: Int): List[Token] = {
      chars match {
        case head :: next => {
          val c = head
          c match
            case '+' => {
              val token = Token(PLUS, c.toString, index)
              loop(next, token, tokens :+ token, index + 1)
            }
            case '-' => {
              val token = Token(MINUS, c.toString, index)
              loop(next, token, tokens :+ token, index  + 1)
            }
            case '/' => {
              val token = Token(SLASH, c.toString, index)
              loop(next, token, tokens :+ token, index  + 1)
            }
            case '*' => {
              val token = Token(STAR, c.toString, index)
              loop(next, token, tokens :+ token, index  + 1)
            }
            case d if d.isDigit && currentToken.dataType != NUMBER => {
              val token = Token(NUMBER, c.toString, index)
              loop(next, token, tokens :+ token, index  + 1)
            }
            case d if d.isDigit && currentToken.dataType == NUMBER => {
              val updatedToken = Token(NUMBER, currentToken.data + c.toString, currentToken.startPos)
              loop(next, updatedToken, tokens.dropRight(1) :+ updatedToken, index + 1)
            }
            case _ => loop(next, Token(NIL, null, index), tokens, index + 1)
        }
        case Nil => {
          tokens :+ Token(EOF, "<EOF>", tokens.last.startPos)
        }
      }
    }
    loop(inputChars, Token(NIL, null, 0), List.empty[Token], 0).filter(_.dataType != NIL)
}
