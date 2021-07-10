package Evaluator.Exceptions.NotFound

class ReturnNotFoundRuntimeException(val functionName : String) : Exception("Couldn't find return statement in function: '$functionName'")