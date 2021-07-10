package Evaluator.Exceptions

import Parser.ParserToken.Operator

class OperationRuntimeException(message : String, operator : Operator) : Exception("$message Operator:'$operator'")