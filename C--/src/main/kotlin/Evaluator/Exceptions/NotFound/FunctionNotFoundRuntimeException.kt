package Evaluator.Exceptions.NotFound

class FunctionNotFoundRuntimeException(val functionName : String) : Exception("Couldn't find Function: '$functionName'")