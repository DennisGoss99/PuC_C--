package Evaluator.Exceptions.NotFound

class VariableNotFoundRuntimeException (val variableName : String) : Exception("Couldn't find variable: '$variableName'")