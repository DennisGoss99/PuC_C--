package Evaluator.Exceptions

import Parser.ParserToken.Parameter


class FunctionParameterRuntimeException(val functionName : String, val parameters : List<Parameter>?) : Exception("Function '$functionName' has to many or few parameter: [$parameters]")