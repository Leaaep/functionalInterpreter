package org.example
package types

enum TokenType {
  // operators
  case PLUS
  case MINUS
  case SLASH
  case STAR
  case EQUAL
  case BANG
  case EQUAL_EQUAL
  case BANG_EQUAL
  case GREATER
  case GREATER_EQUAL
  case LESS
  case LESS_EQUAL

  // data types
  case NUMBER
  case STRING
  case TRUE
  case FALSE
  case NIL

  // special
  case LEFT_PAREN
  case RIGHT_PAREN
  case EOF
}